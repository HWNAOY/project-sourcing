import axios from "axios";
import React, { useEffect, useState } from "react";
import MyChart from "./MyChart";
import CarrierChart from "./CarrierChart";
import { useLocation, useNavigate } from "react-router-dom";
import "../styling/FinalOutput.css";
import MyTable from "./MyTable";
import BarChart from "./BarChart";
import OldTable from "./OldTable";

function FinalOutput() {
  const [result, setresult] = useState([]);
  const [carriers, setcarriers] = useState([]);
  const [lanes, setlanes] = useState([]);
  const [chartType, setchartType] = useState(0);

  const location = useLocation();
  const [pid, setpid] = useState(location.state);
  let navigate = useNavigate();
  //let pid = location.state;
  useEffect(() => {
    loadResults();
  }, []);
  const loadResults = async () => {
    // console.log(pid);
    const newresult = await axios.get(`http://localhost:8080/output/${pid.id}`);
    const newcarrier = await axios.get(
      `http://localhost:8080/capacity/${pid.id}`
    );
    const newlane = await axios.get(`http://localhost:8080/lanes/${pid.id}`);
    const newpid = await axios.get(`http://localhost:8080/processId/${pid.id}`);

    setresult(newresult.data);
    setcarriers(newcarrier.data);
    setlanes(newlane.data);
    setpid(newpid.data);
  };

  const handleHome = (e) => {
    e.preventDefault();
    navigate("/");
  };

  const selectPie = (e) => {
    e.preventDefault();
    setchartType(0);
  }
  const selectBar = (e) => {
    e.preventDefault();
    setchartType(1);
  }
  return (
    <div className="final-output-container">
      <div className="result-info">
        <div className="text">
          <p>The Final Solver Time is {pid.solverTime} in milliseconds</p>
          <p>The Final Total Cost is {pid.finalCost}</p>
        </div>
        <div className="button">
          <button onClick={handleHome} className="back-button">
            Back to Home
          </button>
        </div>
      </div>
      <br />
      <MyTable result={result} lanes={lanes} />
      {/* <OldTable result={result} lanes={lanes}/> */}
      <br/>

      <button className="chart-button" onClick={selectPie}>Show Pie Chart</button>
      <button className="chart-button" onClick={selectBar}>Show Bar Chart</button>
      {!chartType && <div className="charts-container">
        <div className="chart">
          <MyChart result={result} lanes={lanes} carriers={carriers} />
        </div>
        <div className="chart">
          <CarrierChart result={result} lanes={lanes} carriers={carriers} />
        </div>
      </div>}
      {chartType && <BarChart result={result} lanes={lanes} carriers={carriers} />}
    </div>
  );
}

export default FinalOutput;
