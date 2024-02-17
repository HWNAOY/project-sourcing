package com.blumeglobal.springor.Utility;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.springframework.stereotype.Service;

@Service
public class CellHandle {
    public Object getmcell(Cell cell,int f){
        if(cell==null)
            return (f==1) ? "":(double) 0;
        CellType cellType = cell.getCellType().equals(CellType.FORMULA)
                ? cell.getCachedFormulaResultType() : cell.getCellType();
        if(cellType==CellType.NUMERIC)
            return (double) cell.getNumericCellValue();
        else if(cellType==CellType.STRING)
            return cell.getStringCellValue().trim();
        return (f==1) ? "":(double) 0;
    }
}
