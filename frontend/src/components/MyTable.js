import React, { useMemo } from 'react';
import { useTable, useSortBy, useGlobalFilter, usePagination } from 'react-table';
import GlobalFilter from './GlobalFilter';
import '../styling/MyTable.css'

// import 'react-table/react-table.css'; // Import the default styles

function MyTable({ result, lanes }) {
  const columns = useMemo(
    () => [
      {
        Header: '#',
        accessor: 'id',
      },
      {
        Header: 'Carrier',
        accessor: 'carrier',
      },
      {
        Header: 'LaneId',
        accessor: 'laneid',
      },
      {
        Header: 'From/To',
        accessor: 'from_to',
      },
      {
        Header: 'Awarded Volume',
        accessor: 'shipments',
      },
      {
        Header: 'Commitment',
        accessor: 'commitment',
      },
      {
        Header: 'Specific Cost',
        accessor: 'specificCost',
      },
    ],
    []
  );

  const data = useMemo(() => {
    return result.map((item, index) => ({
      id: index + 1,
      carrier: item.carrier,
      laneid: item.laneid,
      from_to: `${lanes[item.laneid - 1].from} to ${lanes[item.laneid - 1].to}`,
      shipments: item.shipments,
      commitment: item.commitment,
      specificCost: item.specificCost,
    }));
  }, [result, lanes]);

  const {
    getTableProps,
    getTableBodyProps,
    headerGroups,
    page,
    prepareRow,
    state,
    setGlobalFilter,
    previousPage,
    nextPage,
    canPreviousPage,
    canNextPage,
  } = useTable(
    {
      columns,
      data,
      initialState: { pageSize: 10 }, // Adjust the page size as needed
    },
    useGlobalFilter,
    useSortBy,
    usePagination
  );

  return (
    <div>
      <div className="global-filter-container">
        <GlobalFilter filter={state.globalFilter} setFilter={setGlobalFilter} />
      </div>
      <table {...getTableProps()} className="result-table">
        <thead>
          {headerGroups.map((headerGroup) => (
            <tr {...headerGroup.getHeaderGroupProps()}>
              {headerGroup.headers.map((column) => (
                <th {...column.getHeaderProps(column.getSortByToggleProps())}>
                  {column.render('Header')}
                  <span>
                    {column.isSorted
                      ? column.isSortedDesc
                        ? ' 🔽'
                        : ' 🔼'
                      : ''}
                  </span>
                </th>
              ))}
            </tr>
          ))}
        </thead>
        <tbody {...getTableBodyProps()}>
          {page.map((row) => {
            prepareRow(row);
            return (
              <tr {...row.getRowProps()}>
                {row.cells.map((cell) => {
                  return (
                    <td {...cell.getCellProps()}>{cell.render('Cell')}</td>
                  );
                })}
              </tr>
            );
          })}
        </tbody>
      </table>
      <div className="pagination">
        <span>
          Page{' '}
          <strong>
            {state.pageIndex + 1} of {Math.ceil(data.length / state.pageSize)}
          </strong>
        </span>
        <button
          className={`pagination-button ${!canPreviousPage ? "disabled" : ""}`}
          onClick={() => previousPage()}
          disabled={!canPreviousPage}
        >
          Previous
        </button>
        <button
          className={`pagination-button ${!canNextPage ? "disabled" : ""}`}
          onClick={() => nextPage()}
          disabled={!canNextPage}
        >
          Next
        </button>
      </div>
    </div>
  );
}

export default MyTable;
