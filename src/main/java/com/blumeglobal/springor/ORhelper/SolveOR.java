package com.blumeglobal.springor.ORhelper;
import com.blumeglobal.springor.exception.EntityNotFoundException;
import com.blumeglobal.springor.models.*;
import com.blumeglobal.springor.repository.*;
import com.blumeglobal.springor.service.ConstraintService;
import com.blumeglobal.springor.service.LocationService;
import com.blumeglobal.springor.service.ResponseService;
import com.blumeglobal.springor.models.*;
import com.blumeglobal.springor.repository.*;
import com.google.ortools.Loader;
import com.google.ortools.linearsolver.MPConstraint;
import com.google.ortools.linearsolver.MPObjective;
import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.linearsolver.MPVariable;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.lucene.util.SloppyMath;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
public class SolveOR {
    @Autowired
    private CapacityRepo capacityRepo;
    @Autowired
    private ConstraintRepo constraintRepo;
    @Autowired
    private LaneRepo laneRepo;
    @Autowired
    private LocationRepo locationRepo;
    @Autowired
    private ResponseRepo responseRepo;
    @Autowired
    private ResponseService responseService;
    @Autowired
    private ConstraintService constraintService;
    @Autowired
    private LocationService locationService;
    @Autowired
    private FinalOutputRepo finalOutputRepo;
    @Autowired
    private ProcessIdRepo processIdRepo;
    public enum Specify{
        VOLUME,PERCENTAGE,NUM_CARRIERS
    }
    public enum Ctype{
        M,W,N
    }
    public enum Minormax{
        MAX,MIN
    }
    public List<FinalOutput> solution(ProcessId lastid) throws IOException {
        Loader.loadNativeLibraries();
        double infinity = Double.POSITIVE_INFINITY;
//        ProcessId lastid = processIdRepo.findFirstByOrderByIdDesc();
//        System.out.println(lastid.getProcessId());
        List<Capacity> capacities = capacityRepo.findByProcessId(lastid);
        List<Constraint> constraints = constraintRepo.findByProcessId(lastid);
        List<Lanes> lanes = laneRepo.findByProcessId(lastid);
        List<Location> locations = locationRepo.findByProcessId(lastid);
        int numCarriers = capacities.size();
        int numLanes = lanes.size();
        MPSolver solver = MPSolver.createSolver("SCIP");
        MPVariable[][] x = new MPVariable[numCarriers][numLanes];
        MPVariable[][] nc = new MPVariable[numCarriers][numLanes];
//        MPVariable[][] wc = new MPVariable[numCarriers][numLanes];
        MPVariable[] uv = new MPVariable[numLanes];

        Map<Pair<String,Long>, Response> mp = responseService.getCarrierResponseMap(lastid);
        // Creating Variables

        for(int c=0;c<numCarriers;c++){
            for(int l=0;l<numLanes;l++){
                String name = "CL_"+capacities.get(c).getCarrier()+" "+lanes.get(l).getLaneid();
                String name_nc = "NC_"+capacities.get(c).getCarrier()+" "+lanes.get(l).getLaneid();
                x[c][l] = solver.makeIntVar(0,infinity,name);
                nc[c][l] = solver.makeIntVar(0,1,name_nc);
//                wc[c][l] = solver.makeIntVar(0,1,"");
            }
        }
        for(int l=0;l<numLanes;l++){
            String name = "UV_"+lanes.get(l).getLaneid();
            uv[l] = solver.makeNumVar(0,infinity,name);
        }
        // Constraint for commitment and capacity
        for(int c=0;c<numCarriers;c++){
            String name_capcons = "Capacity-constraint "+capacities.get(c).getCarrier();
            MPConstraint constraint1 = solver.makeConstraint(0,capacities.get(c).getCapacity(),name_capcons);
            String carrier=capacities.get(c).getCarrier();
            for(int l=0;l<numLanes;l++){
                Long laneid = lanes.get(l).getLaneid();
                Response newresponse = mp.get(new Pair<>(carrier,laneid));
                Long commitment = newresponse.getCommitment();
                String name_comcons = "Commitment-constraint "+carrier+" "+laneid;
                MPConstraint constraint2 = solver.makeConstraint(0,commitment,name_comcons);
                //double cof = rate*dis*(1-0.6);
                constraint1.setCoefficient(x[c][l],1);
                constraint2.setCoefficient(x[c][l],1);
            }
        }
        // volume constraint
        for(int l=0;l<numLanes;l++){
            Long volume = lanes.get(l).getVolume();
            String name_volcons = "Volume-constraint "+lanes.get(l).getLaneid();
            MPConstraint constraint3 = solver.makeConstraint(volume,volume,name_volcons);
            for(int c=0;c<numCarriers;c++){
                constraint3.setCoefficient(x[c][l],1);
            }
            constraint3.setCoefficient(uv[l],1);
        }
        // New constraints logic
        Map<String,Integer> mp5 = new HashMap<>();
        Map<Long,Integer> mp6 = new HashMap<>();
        int k= 0;
        for(Capacity capacity:capacities) {
            mp5.put(capacity.getCarrier(), k);
            k++;
        }
        k= 0;
        for(Lanes lane:lanes) {
            mp6.put(lane.getLaneid(), k);
            k++;
        }
        k=0;
        for(Constraint constraint:constraints){
            k++;
            List<String> capacities1 = new ArrayList<>();
            List<Long> lanes1 = new ArrayList<>();
            if(constraint.getCarrier().isEmpty()){

                for(Capacity capacity:capacities)
                    capacities1.add(capacity.getCarrier());
            }
            else
                capacities1.add(constraint.getCarrier());
            if(constraint.getLaneid()==(long)0){
                for(Lanes lane:lanes)
                    lanes1.add(lane.getLaneid());
            }
            else
                lanes1.add(constraint.getLaneid());
            String ttype = constraint.getType();
            String constraintType = constraint.getConstraintType();
            String minimax = constraint.getMinimax();
            Long value = constraint.getValue();
            String name_constable = "Constraints table "+k;
            if(Specify.valueOf(ttype)==Specify.VOLUME){

                if(Ctype.valueOf(constraintType)==Ctype.M){

                    if(Minormax.valueOf(minimax)==Minormax.MAX){

                        for(String capacity:capacities1){
                            for(Long lane:lanes1){
                                int c = mp5.get(capacity);
                                int l = mp6.get(lane);

                                MPConstraint constraint4 = solver.makeConstraint(0,value,name_constable);
                                constraint4.setCoefficient(x[c][l],1);
                            }
                        }
                    }
                    else if(Minormax.valueOf(minimax)==Minormax.MIN){
                        for(String capacity:capacities1) {
                            for (Long lane : lanes1) {
                                int c = mp5.get(capacity);
                                int l = mp6.get(lane);
                                MPConstraint constraint4 = solver.makeConstraint(value, infinity, name_constable);
                                constraint4.setCoefficient(x[c][l], 1);
                            }
                        }
                    }

                }
                else if(Ctype.valueOf(constraintType)==Ctype.W){

                    if(Minormax.valueOf(minimax)==Minormax.MAX){
                        for(String capacity:capacities1) {
                            for (Long lane : lanes1) {
                                int c = mp5.get(capacity);
                                int l = mp6.get(lane);
                                MPConstraint constraint4 = solver.makeConstraint(-infinity, 0, name_constable);
                                constraint4.setCoefficient(x[c][l], 1);
                                constraint4.setCoefficient(nc[c][l], -value);
                            }
                        }
                    }
                    else if(Minormax.valueOf(minimax)==Minormax.MIN){
                        for(String capacity:capacities1) {
                            for (Long lane : lanes1) {
                                int c = mp5.get(capacity);
                                int l = mp6.get(lane);
                                MPConstraint constraint4 = solver.makeConstraint(0, infinity, name_constable);
                                constraint4.setCoefficient(x[c][l], 1);
                                constraint4.setCoefficient(nc[c][l], -value);
                            }
                        }
                    }
                }
            }
            else if(Specify.valueOf(ttype)==Specify.PERCENTAGE){
                if(Ctype.valueOf(constraintType)==Ctype.M){
//                                System.out.println(percentValue);
                    if(Minormax.valueOf(minimax)==Minormax.MAX){
                        for(String capacity:capacities1) {
                            for (Long lane : lanes1) {
                                int c = mp5.get(capacity);
                                int l = mp6.get(lane);
                                Long volume = lanes.get(l).getVolume();
                                Long percentValue=value*volume;
                                MPConstraint constraint4 = solver.makeConstraint(0, percentValue, name_constable);
                                constraint4.setCoefficient(x[c][l], 100);
                            }
                        }
                    }
                    else if(Minormax.valueOf(minimax)==Minormax.MIN){
                        for(String capacity:capacities1) {
                            for (Long lane : lanes1) {
                                int c = mp5.get(capacity);
                                int l = mp6.get(lane);
                                Long volume = lanes.get(l).getVolume();
                                Long percentValue=value*volume;
                                MPConstraint constraint4 = solver.makeConstraint(percentValue, infinity, name_constable);
                                constraint4.setCoefficient(x[c][l], 100);
                            }
                        }
                    }
                }
                else if(Ctype.valueOf(constraintType)==Ctype.W){

                    if(Minormax.valueOf(minimax)==Minormax.MAX){
                        for(String capacity:capacities1) {
                            for (Long lane : lanes1) {
                                int c = mp5.get(capacity);
                                int l = mp6.get(lane);
                                Long volume = lanes.get(l).getVolume();
                                Long percentValue=value*volume;
                                MPConstraint constraint4 = solver.makeConstraint(-infinity, 0, name_constable);
                                constraint4.setCoefficient(x[c][l], 100);
                                constraint4.setCoefficient(nc[c][l], -percentValue);
                            }
                        }
                    }
                    else if(Minormax.valueOf(minimax)==Minormax.MIN){

                        for(String capacity:capacities1) {
                            for (Long lane : lanes1) {
                                int c = mp5.get(capacity);
                                int l = mp6.get(lane);
                                Long volume = lanes.get(l).getVolume();
                                Long percentValue=value*volume;
                                MPConstraint constraint4 = solver.makeConstraint(0, infinity, name_constable);
                                constraint4.setCoefficient(x[c][l], 100);
                                constraint4.setCoefficient(nc[c][l], -percentValue);
                            }
                        }
                    }
                }
            }
            else if(Specify.valueOf(ttype)==Specify.NUM_CARRIERS){
                if(Ctype.valueOf(constraintType)==Ctype.N){
                    if(Minormax.valueOf(minimax)==Minormax.MAX){
                        for(Long lane:lanes1) {
                            MPConstraint constraint4 = solver.makeConstraint(0, value, name_constable);
                            for (String capacity:capacities1) {
                                int c = mp5.get(capacity);
                                int l = mp6.get(lane);
                                constraint4.setCoefficient(nc[c][l], 1);
                            }
                        }
                    }
                    else if(Minormax.valueOf(minimax)==Minormax.MIN){
                        for(Long lane:lanes1) {
                            MPConstraint constraint4 = solver.makeConstraint(value,infinity,name_constable);
                            for (String capacity:capacities1) {
                                int c = mp5.get(capacity);
                                int l = mp6.get(lane);
                                constraint4.setCoefficient(nc[c][l], 1);
                            }
                        }
                    }
                }
            }
        }



        // lc-nc<=0
        // Clarify this doubt for the formulation
        for(int c=0;c<numCarriers;c++){
            for(int l=0;l<numLanes;l++){
                Long volume = lanes.get(l).getVolume();
                String carr = capacities.get(c).getCarrier();
                Long laneid = lanes.get(l).getLaneid();
                String name_wincons = "Winning Constraint "+carr+" "+laneid;
                MPConstraint constraint5 = solver.makeConstraint(-infinity,0,name_wincons);
                constraint5.setCoefficient(x[c][l], 1);
                constraint5.setCoefficient(nc[c][l],-volume);
            }
        }
//
//        for(int l=0;l<numLanes;l++){
//            MPConstraint constraint6 = solver.makeConstraint(0,1,"");
//            for(int c=0;c<numCarriers;c++){
//                constraint6.setCoefficient(wc[c][l],1);
//            }
//        }
        // Objective function
        MPObjective objective = solver.objective();
        double[] scores = new double[numCarriers];
        long maxScore = Long.MIN_VALUE;
        long minScore = Long.MAX_VALUE;
        for(int i=0;i<numCarriers;i++){
            scores[i] = capacities.get(i).getBlumeScore();
            maxScore = Math.max((long)scores[i],maxScore);
            minScore = Math.min((long)scores[i],minScore);
        }
        for(int i=0;i<numCarriers;i++){
            scores[i]=(scores[i]-minScore)/(maxScore-minScore);
        }
        Map<String,Location> mp3 = locationService.getLocationMap(lastid);
        double sumRate = 0,sumDis=0;
        for(int c=0;c<numCarriers;c++){
            String carrier=capacities.get(c).getCarrier();
            for(int l=0;l<numLanes;l++){
                Response newresponse = mp.get(new Pair<>(carrier,(long)l+1));
                Long rate = newresponse.getRate();
                String from = lanes.get(l).getFrom();
                String to = lanes.get(l).getTo();
                // Create Map for this
                double lat1 = mp3.get(from).getLatitude();
                double long1 = mp3.get(from).getLongitude();
                double lat2 = mp3.get(to).getLatitude();
                double long2 = mp3.get(to).getLongitude();
                double dis = (long)(SloppyMath.haversinMeters(lat1,long1,lat2,long2)* 0.000621371192);
                double cof = rate*dis*(1-0.5*scores[c]);
                objective.setCoefficient(x[c][l],cof);
                sumRate+=rate;
                sumDis+=dis;
            }
        }
        for(int l=0;l<numLanes;l++){
            double cof = Math.pow(10,10);
            objective.setCoefficient(uv[l],cof);
        }
        objective.setMinimization();

//        System.out.println(solver.exportModelAsLpFormat());
        String lpFormat = solver.exportModelAsLpFormat();
        String path  = "C:/Users/harsh.gupta/Blume_projects/project-optimized-sourcing/src/main/java/com/blumeglobal/springor/ORhelper/output.txt";
        File file = new File(path);
        File parentDirectory = file.getParentFile();
        if(parentDirectory != null && parentDirectory.exists()) {
            FileWriter fileWriter = new FileWriter(path);
            fileWriter.write(lpFormat);
            fileWriter.close();
        }
        else{
            System.out.println("Directory does not exist");
        }

        MPSolver.ResultStatus resultStatus = solver.solve();
        if(resultStatus == MPSolver.ResultStatus.OPTIMAL || resultStatus == MPSolver.ResultStatus.FEASIBLE){
            System.out.println("Total cost: " + objective.value() + "\n");
            for (int i = 0; i < numCarriers; ++i) {
                String carrier  = capacities.get(i).getCarrier();
                for (int j = 0; j < numLanes; ++j) {
                    Long laneid = lanes.get(j).getLaneid();
                    System.out.println("The cost for carrier "+ carrier + " for lane " + laneid + " is " +x[i][j].solutionValue());
                }
            }
        }else {
            System.err.println("No solution found.");
        }
        for(int c=0;c<numCarriers;c++){
            for(int l=0;l<numLanes;l++){
                String carrier = capacities.get(c).getCarrier();
                Long laneid = lanes.get(l).getLaneid();
                System.out.println(nc[c][l].solutionValue()+" "+ carrier+" "+ laneid);
            }
        }
//        System.out.println(nc[1][2].solutionValue());
        double solverTime = solver.wallTime();
        double finalCost = 0;
        double[][] ship = new double[numCarriers][numLanes];
        for(int c=0;c<numCarriers;c++){
            String carrier=capacities.get(c).getCarrier();
            for(int l=0;l<numLanes;l++){
                Response newresponse = mp.get(new Pair<>(carrier,(long)l+1));
                Long rate = newresponse.getRate();
                String from = lanes.get(l).getFrom();
                String to = lanes.get(l).getTo();
                // Create Map for this
                double lat1 = mp3.get(from).getLatitude();
                double long1 = mp3.get(from).getLongitude();
                double lat2 = mp3.get(to).getLatitude();
                double long2 = mp3.get(to).getLongitude();
                double dis = (long)(SloppyMath.haversinMeters(lat1,long1,lat2,long2)* 0.000621371192);
                double cof = rate*dis;
//                objective.setCoefficient(x[c][l],cof);
                ship[c][l] = x[c][l].solutionValue();
                finalCost+=(cof*ship[c][l]);
            }
        }
        ProcessId newprocessId = processIdRepo.findById(lastid.getId())
                .orElseThrow(() -> new EntityNotFoundException(lastid.getId()));
        newprocessId.setFinalCost(finalCost);
        newprocessId.setSolverTime(solverTime);
        processIdRepo.save(newprocessId);

        List<FinalOutput> finalOutputs = finalOutputRepo.findByProcessId(lastid);
        for(int c=0;c<numCarriers;c++){
            String carrier = capacities.get(c).getCarrier();
            for(int l=0;l<numLanes;l++){
                FinalOutput finalOutput = new FinalOutput();

                Long laneid = lanes.get(l).getLaneid();
                int f = numLanes*c+l;
                if(!finalOutputs.isEmpty())
                    finalOutput.setId(finalOutputs.get(f).getId());

                finalOutput.setCarrier(carrier);
                finalOutput.setLaneid(laneid);
                finalOutput.setShipments(ship[c][l]);
                finalOutput.setProcessId(lastid);

                Response newresponse = mp.get(new Pair<>(carrier,(long)l+1));
                Long rate = newresponse.getRate();
                String from = lanes.get(l).getFrom();
                String to = lanes.get(l).getTo();
                Long commitment = newresponse.getCommitment();
                // Create Map for this
                double lat1 = mp3.get(from).getLatitude();
                double long1 = mp3.get(from).getLongitude();
                double lat2 = mp3.get(to).getLatitude();
                double long2 = mp3.get(to).getLongitude();
                double dis = (long)(SloppyMath.haversinMeters(lat1,long1,lat2,long2)* 0.000621371192);
                double cof = rate*dis;
                finalOutput.setSpecificCost(cof*x[c][l].solutionValue());
                finalOutput.setCommitment(commitment);

                finalOutputRepo.save(finalOutput);
            }
        }

        return finalOutputRepo.findByProcessId(lastid);
    }
}
