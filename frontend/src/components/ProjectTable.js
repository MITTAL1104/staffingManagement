import React from "react";
import { usePagination, useSortBy, useTable } from "react-table";
import "../styles/common.css";

const ProjectTable = ({ data }) => {
  const columns = React.useMemo(
    () => [
      { Header: "ID", accessor: "projectId", width: 50 },
      { Header: "Project Name", accessor: "projectName", width: 80 },
      { Header: "Description", accessor: "description", width: 100 },
      { Header: "Owner Name", accessor: "projectOwnerName", width: 70 },
      { Header: "Start Date", accessor: "startDate", width: 50 },
      { Header: "End Date", accessor: "endDate", width: 50 },
      {
        Header: "Active",
        accessor: "isActive",
        width: 20,
        Cell: ({ value }) => (
          <span className="cell-boolean">{value ? "✅" : "❌"}</span>
        ),
        sortType: (rowA, rowB) => {
          const valA = rowA.values.isActive ? 1 : 0;
          const valB = rowB.values.isActive ? 1 : 0;
          return valB - valA; // Ticks first
        },
      },
    ],
    []
  );

  const { getTableProps, getTableBodyProps, headerGroups, page, prepareRow, canPreviousPage, canNextPage, pageOptions, pageCount, gotoPage, nextPage, previousPage, setPageSize, state: { pageIndex, pageSize } } = 
    useTable(
      {
        columns,
        data: data || [],
        initialState : {pageIndex:0,pageSize:5},
      },
      useSortBy,
      usePagination
    );

  return (
    <div>
      <table
        {...getTableProps()}
        style={{ width: "100%", borderCollapse: "collapse" }}
      >
        <thead>
          {headerGroups.map((headerGroup) => (
            <tr {...headerGroup.getHeaderGroupProps()} key={headerGroup.id}>
              {headerGroup.headers.map((column) => (
                <th
                  {...column.getHeaderProps(column.getSortByToggleProps)}
                  key={column.id}
                  style={{ width: column.width }}
                >
                  {column.render("Header")}
                  <span>
                    {column.isSorted
                      ? column.isSortedDesc
                        ? " 🔽"
                        : " 🔼"
                      : " ⬍"}
                  </span>
                </th>
              ))}
            </tr>
          ))}
        </thead>
        <tbody {...getTableBodyProps()}>
          {page.map((row) => {
            prepareRow(row);
            const isActive = row.original.isActive;
            return (
              <tr
                {...row.getRowProps()}
                key={row.id}
                style={{
                  backgroundColor: isActive ? "#e6ffed" : "transparent",
                  color: isActive? "black":"grey"
                }}
              >
                {row.cells.map((cell) => (
                  <td
                    {...cell.getCellProps()}
                    key={cell.column.id}
                    style={{ width: cell.column.width }}
                  >
                    {cell.render("Cell")}
                  </td>
                ))}
              </tr>
            );
          })}
        </tbody>
      </table>

      <div className="pagination" style={{ marginTop: "10px", display: "flex", justifyContent: "space-between", alignItems: "center" }}>
        <div>
          <button onClick={() => gotoPage(0)} disabled={!canPreviousPage}>{"<<"}</button>
          <button onClick={() => previousPage()} disabled={!canPreviousPage}>Previous</button>
          <button onClick={() => nextPage()} disabled={!canNextPage}>Next</button>
          <button onClick={() => gotoPage(pageCount - 1)} disabled={!canNextPage}>{">>"}</button>
        </div>

        <div>
          <span>
            Page{" "}
            <strong>
              {pageIndex + 1} of {pageOptions.length}
            </strong>{" "}
          </span>
          | Go to page:{" "}
          <input
            type="number"
            min="1"
            max={pageCount}
            defaultValue={pageIndex + 1}
            onChange={(e) => {
              const pageNumber = e.target.value ? Number(e.target.value) - 1 : 0;
              gotoPage(pageNumber);
            }}
            style={{ width: "50px" }}
          />
        </div>

        <div>
          <select
            value={pageSize}
            onChange={(e) => setPageSize(Number(e.target.value))}
          >
            {[5, 10, 15, 20].map((size) => (
              <option key={size} value={size}>
                Show {size}
              </option>
            ))}
          </select>
        </div>
      </div>
    </div>
  );
};

export default ProjectTable;
