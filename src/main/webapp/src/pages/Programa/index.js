import { Link } from "react-router-dom";
import React from "react";
import {
  Button,
  Card,
  Table,
  ButtonGroup,
  InputGroup,
  FormControl,
} from "react-bootstrap";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faEdit,
  faList,
  faTrash,
  faPlusCircle,
  faStepForward,
  faStepBackward,
  faFastForward,
  faFastBackward,
} from "@fortawesome/free-solid-svg-icons";
import api from "../../services/api";
import { useEffect } from "react";
import { useState } from "react";
import MyToast from "../../components/MyToast";

function Programa() {
  const [programas, setProgramas] = useState([]);
  const [currentPage, setCurrentPage] = useState(1);
  const [programasPerPage] = useState(5);
  const [totalPages, setTotalPages] = useState(null);
  const [totalElements, setTotalElements] = useState(null);
  const [show, setShow] = useState(false);

  useEffect(() => findAllProgramas(currentPage), [programasPerPage]);

  function findAllProgramas(currentPage) {
    currentPage -= 1;
    api
      .get(`programa?page=${currentPage}&size=${programasPerPage}`)
      .then((response) => response.data)
      .then((data) => {
        setProgramas(data.content);
        setTotalPages(data.totalPages);
        setTotalElements(data.totalElements);
        setCurrentPage(data.number + 1);
      });
  }

  function deletePrograma(id) {
    api.delete(`programa/${id}`).then((response) => {
      if (response.data !== null) {
        setProgramas(programas.filter((programa) => programa.id !== id));
        setShow(true);
        setTimeout(() => setShow(false), 3000);
      } else {
        setShow(false);
      }
    });
  }

  function changePage(event) {
    let targetPage = parseInt(event.target.value || "1");
    findAllProgramas(targetPage);
    setCurrentPage(targetPage);
  }

  function firstPage() {
    let firstPage = 1;
    if (currentPage > firstPage) {
      findAllProgramas(firstPage);
    }
  }

  function prevPage() {
    let prevPage = 1;
    if (currentPage > prevPage) {
      findAllProgramas(currentPage - prevPage);
    }
  }

  function nextPage() {
    if (currentPage < Math.ceil(totalElements / programasPerPage)) {
      findAllProgramas(currentPage + 1);
    }
  }

  function lastPage() {
    let condition = Math.ceil(totalElements / programasPerPage);
    if (currentPage < condition) {
      findAllProgramas(condition);
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
      <div style={{ display: show ? "block" : "none" }}>
        <MyToast
          show={show}
          menssagem={"Programa Excluído com SUCESSO!"}
          type={"danger"}
        />
      </div>
      <Card className={"border border-dark bg-dark text-white"}>
        <Card.Header>
          <FontAwesomeIcon icon={faList} />
          {"  "}Lista de Programas
        </Card.Header>
        <Card.Body>
          <Table bordered hover striped variant="dark">
            <thead>
              <tr>
                <th>Id</th>
                <th>Nome</th>
                <th>Ano</th>
                <th>Ações</th>
              </tr>
            </thead>
            <tbody>
              {programas.length === 0 ? (
                <tr align="center">
                  <td colSpan="4">Sem programas</td>
                </tr>
              ) : (
                programas.map((programa) => (
                  <tr key={programa.id}>
                    <td>{programa.id}</td>
                    <td>{programa.nome}</td>
                    <td>{programa.ano}</td>
                    <td>
                      <ButtonGroup
                        style={{
                          display: "flex",
                          justifyContent: "space-between",
                        }}
                      >
                        <Link
                          to={"/programa/form/" + programa.id}
                          className="btn btn-md btn-outline-primary"
                        >
                          <FontAwesomeIcon icon={faEdit} />
                        </Link>
                        <Button
                          size="md"
                          variant="outline-danger"
                          onClick={() => deletePrograma(programa.id)}
                        >
                          <FontAwesomeIcon icon={faTrash} />
                        </Button>
                      </ButtonGroup>
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </Table>
          <Link to={"/programa/form"} className="nav-link">
            <Button block>
              <FontAwesomeIcon icon={faPlusCircle} /> Novo Programa
            </Button>
          </Link>
        </Card.Body>
        <Card.Footer>
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
        </Card.Footer>
      </Card>
    </div>
  );
}

export default Programa;
