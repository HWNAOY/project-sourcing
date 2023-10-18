import React, { useState } from "react";
import {
  Chart,
  ChartTitle,
  ChartLegend,
  ChartSeries,
  ChartSeriesItem,
  ChartSeriesItemTooltip,
  ChartTooltip,
} from "@progress/kendo-react-charts"; // Import the necessary chart components
import "hammerjs";
import ReactPaginate from "react-paginate";
const CarrierChart = ({ result, lanes, carriers }) => {
  const chartData = [];
  const [currentPage, setCurrentPage] = useState(0);
  const itemsPerPage = 2;
  const totalPages = Math.ceil(carriers.length / itemsPerPage);
  const startIndex = currentPage * itemsPerPage;
  const endIndex = Math.min(startIndex + itemsPerPage, carriers.length);
  const subset = chartData.slice(startIndex, endIndex);

  const handlePageChange = ({ selected }) => {
    setCurrentPage(selected);
  };
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
  //   console.log('result:', result);
  //   console.log('lanes:', lanes);
  //     console.log(carriers);
  // const labelContent = ({dataItem}) => {
  //     if(dataItem.category === 'Left Over'){
  //         return 'Left Over: ${props.dataItem.value}';
  //     }
  //     return dataItem.value;
  // };

  for (let i = startIndex; i < endIndex; i++) {
    const series = [];
    let diff = 0;
    for (let j = 0; j < result.length; j++) {
      if (carriers[i].carrier === result[j].carrier) {
        let id = result[j].laneid;
        series.push({
          lane: `${id}: ${lanes[id - 1].from} to ${lanes[id - 1].to}`,
          value: result[j].shipments,
        });
        diff += result[j].shipments;
      }
    }
    diff = carriers[i].capacity - diff;
    if (diff > 0) {
      series.push({ lane: "Left Over", value: diff });
    }
    // console.log(lanes.length);
    const titleText = `Shipments by Carrier: ${carriers[i].carrier}`;
    chartData.push(
      <Chart key={i}>
        <ChartTitle text={titleText} position="top" />
        <ChartLegend position="bottom" />
        <ChartTooltip render={renderTooltip} />
        <ChartSeries>
          <ChartSeriesItem
            type="pie"
            data={series}
            field="value"
            categoryField="lane"
            labels={{
              visible: true,
              // content: ({dataItem}) => labelContent,
            }}
            // tooltip={{
            //   visible: true,
            //   render: {renderTooltip}
            //   // template: "#= category # - #= value #",
            //   // format: "{0}"
            // }}
          ></ChartSeriesItem>
        </ChartSeries>
      </Chart>
    );
  }

  return (
    <div>
      {chartData}
      <ReactPaginate
        previousLabel={"previous"}
        nextLabel={"next"}
        breakLabel={"..."}
        pageCount={totalPages}
        marginPagesDisplayed={2}
        pageRangeDisplayed={5}
        onPageChange={handlePageChange}
        containerClassName={"pagination"}
        subContainerClassName={"pages pagination"}
        activeClassName={"pagination__link--active"}
        pageLinkClassName={"custom-active-link"}
      />
    </div>
  );
};

export default CarrierChart;
