import React from 'react';
import { Chart, ChartTitle, ChartLegend, ChartSeries, ChartSeriesItem, ChartTooltip } from '@progress/kendo-react-charts'; // Import the necessary chart components
import 'hammerjs';
import { useState } from 'react';
import ReactPaginate from 'react-paginate';
import '../styling/MyChart.css'

const MyChart = ({ result, lanes, carriers }) => {
  const chartData = [];
  const [currentPage, setCurrentPage] = useState(0);
  const itemsPerPage = 2;
  const totalPages = Math.ceil(lanes.length/itemsPerPage);
  const startIndex = currentPage * itemsPerPage;
  const endIndex = Math.min(startIndex + itemsPerPage, lanes.length);
  const subset = chartData.slice(startIndex, endIndex);

  const handlePageChange = ({selected}) => {
    setCurrentPage(selected);
  };
  const renderTooltip = (data) => {
    // console.log(data)
    // console.log(data.dataItem);
    if(data.dataItem !== undefined)
      return null;
    
    return(
    <span>
      <b>{data.point.category} : {data.point.value}</b>
    </span>
    )
  }
//   console.log('result:', result);
//   console.log('lanes:', lanes);

  for (let i = startIndex; i <endIndex; i++) {
    const series = [];
    let diff = 0;
    for (let j = 0; j < result.length; j++) {
      if (lanes[i].laneid === result[j].laneid) {
        series.push({ carr: result[j].carrier, value: result[j].shipments });
        diff+=result[j].shipments;
      }
    }
    diff=lanes[i].volume-diff;
    if(diff>0){
        series.push({carr:'Left Over',value:diff})
    }
    // console.log(lanes.length);
    const titleText = `Shipments by lane: ${lanes[i].laneid}: ${lanes[i].from} to ${lanes[i].to}`;
    chartData.push(
      <Chart key={i}>
        <ChartTitle text={titleText} />
        <ChartLegend position='bottom' />
        <ChartTooltip render={renderTooltip}/>
        <ChartSeries>
          <ChartSeriesItem
            type='pie'
            data={series}
            field='value'
            categoryField='carr'
            labels={{
              visible: true,

            }}
            tooltip={{
              visible: true,
              template: "#= category # - #= value #",
              // format: "{0}"
            }}
          />
        </ChartSeries>
      </Chart>
    );

  }


  return (
  <div>
    {chartData}
    <ReactPaginate
        previousLabel={'previous'}
        nextLabel={'next'}
        breakLabel={'...'}
        pageCount={totalPages}
        marginPagesDisplayed={2}
        pageRangeDisplayed={5}
        onPageChange={handlePageChange}
        containerClassName={'pagination'}
        subContainerClassName={'pages pagination'}
        activeClassName={'pagination__link--active'}
        pageLinkClassName={'custom-active-link'}
      />
  </div>
  );
};

export default MyChart;
