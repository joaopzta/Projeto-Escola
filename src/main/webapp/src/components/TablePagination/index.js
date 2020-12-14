import { useState } from "react";
import {
  faStepForward,
  faStepBackward,
  faFastForward,
  faFastBackward,
} from "@fortawesome/free-solid-svg-icons";
import {
  Button,
  InputGroup,
  FormControl,
} from "react-bootstrap";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";

function TablePagination({ currentPage, itensPerPage, totalElements, totalPages, findAllItens }) {
  function changePage(event) {
    let targetPage = parseInt(event.target.value || "1");
    findAllItens(targetPage);
    currentPage = targetPage;
  }

  function firstPage() {
    let firstPage = 1;
    if (currentPage > firstPage) {
      findAllItens(firstPage);
    }
  }

  function prevPage() {
    let prevPage = 1;
    if (currentPage > prevPage) {
      findAllItens(currentPage - prevPage);
    }
  }

  function nextPage() {
    if (currentPage < Math.ceil(totalElements / itensPerPage)) {
      findAllItens(currentPage + 1);
    }
  }

  function lastPage() {
    let condition = Math.ceil(totalElements / itensPerPage);
    if (currentPage < condition) {
      findAllItens(condition);
    }
  }

  const pageNumCss = {
    width: "45px",
    border: "1px solid #17A2B8",
    color: "#17A2B8",
    textAlign: "center",
    fontWeight: "bold",
  };

  return (
    <div>
      <div style={{ float: "left" }}>
        Showing Page {currentPage} of {Math.ceil(totalPages)}
      </div>
      <div style={{ float: "right" }}>
        <InputGroup size="sm">
          <InputGroup.Prepend>
            <Button
              type="button"
              variant="outline-info"
              disabled={currentPage === 1 ? true : false}
              onClick={firstPage}
            >
              <FontAwesomeIcon icon={faFastBackward} /> First
            </Button>
            <Button
              type="button"
              variant="outline-info"
              disabled={currentPage === 1 ? true : false}
              onClick={prevPage}
            >
              <FontAwesomeIcon icon={faStepBackward} /> Prev
            </Button>
          </InputGroup.Prepend>
          <FormControl
            style={pageNumCss}
            className={"bg-dark"}
            name="currentPage"
            value={currentPage}
            onChange={changePage}
          />
          <InputGroup.Append>
            <Button
              type="button"
              variant="outline-info"
              disabled={currentPage === Math.ceil(totalPages) ? true : false}
              onClick={nextPage}
            >
              <FontAwesomeIcon icon={faStepForward} /> Next
            </Button>
            <Button
              type="button"
              variant="outline-info"
              disabled={currentPage === Math.ceil(totalPages) ? true : false}
              onClick={lastPage}
            >
              <FontAwesomeIcon icon={faFastForward} /> Last
            </Button>
          </InputGroup.Append>
        </InputGroup>
      </div>
    </div>
  );
}

export default TablePagination;
