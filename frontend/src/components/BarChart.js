import {
  Chart,
  ChartCategoryAxis,
  ChartCategoryAxisItem,
  ChartSeries,
  ChartSeriesItem,
  ChartTooltip,
} from "@progress/kendo-react-charts";
import React from "react";

function BarChart({ result, lanes, carriers }) {
  const chartData = [];

  const renderTooltip = (data) => {
    // console.log(data)
    // console.log(data.dataItem);
    if (data.dataItem !== undefined) return null;

    return (
      <span>
        <b>
          {data.point.category} : {data.point.value}
        </b>
      </span>
    );
  };

  for (let i = 0; i < carriers.length; i++) {
    const series = [];
    for (let j = 0; j < result.length; j++) {
      if (carriers[i].carrier == result[j].carrier) {
        let id = result[j].laneid;
        series.push({
          lane: `${id}: ${lanes[id - 1].from} to ${lanes[id - 1].to}`,
          value: result[j].shipments,
          carrier: carriers[i].carrier,
        });
      }
    }
    // const titleText = `Shipments by Carrier: ${carriers[i].carrier}`;
    chartData.push(
      <ChartSeriesItem
        key={i}
        type="column"
        data={series}
        field="value"
        categoryField="lane"
        gap={5}
        spacing={0.1}
        labels={{
          visible: true,
          // content: ({dataItem}) => labelContent,
          content: ({ dataItem }) => {
            return `${dataItem.carrier}`;
          },
        }}
      />
    );
  }
  return (
    <div>
      <Chart>
        <ChartCategoryAxis>
          <ChartCategoryAxisItem labels={{ rotation: -45 }} />
        </ChartCategoryAxis>
        <ChartTooltip render={renderTooltip}/>
        <ChartSeries>{chartData}</ChartSeries>
      </Chart>
    </div>
  );
}

export default BarChart;
