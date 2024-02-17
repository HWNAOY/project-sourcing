import React from "react";
import "../styling/FinalOutput.css";

function OldTable({ result, lanes }) {
  return (
    <div>
      <table className="result-table">
        <thead>
          <tr>
            <th scope="col">#</th>
            <th scope="col">carrier</th>
            <th scope="col">laneid</th>
            <th scope="col">Awarded Volume</th>
            <th scope="col">Commitment</th>
            <th scope="col">Specific cost</th>
          </tr>
        </thead>
        <tbody>
          {result.map((item, index) => (
            <tr>
              <th scope="row" key={index}>
                {index + 1}
              </th>
              <td>{item.carrier}</td>
              <td>
                {item.laneid}: {lanes[item.laneid - 1].from} to{" "}
                {lanes[item.laneid - 1].to}
              </td>
              <td>{item.shipments}</td>
              <td>{item.commitment}</td>
              <td>{item.specificCost}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

export default OldTable;
