import React, { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import MyToast from "../../components/MyToast";
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

function Materia() {
  const [materias, setMaterias] = useState([]);
  const [currentPage, setCurrentPage] = useState(1);
  const [materiasPerPage] = useState(5);
  const [show, setShow] = useState(false);

  const lastIndex = currentPage * materiasPerPage;
  const firstIndex = lastIndex - materiasPerPage;
  const currentMaterias = materias.slice(firstIndex, lastIndex);
  const totalPages = materias.length / materiasPerPage;

  useEffect(findAllMaterias, []);

  function findAllMaterias() {
    api
      .get("materia")
      .then((response) => response.data)
      .then((data) => {
        setMaterias(data);
      });
  }

  function deleteMateria(id) {
    api.delete(`materia/${id}`).then((response) => {
      if (response.data !== null) {
        setMaterias(materias.filter((materia) => materia.id !== id));
        setShow(true);
        setTimeout(() => setShow(false), 3000);
      } else {
        setShow(false);
      }
    });
  }

  function changePage(event) {
    setCurrentPage(parseInt(event.target.value || "1"));
  }

  function firstPage() {
    if (currentPage > 1) {
      setCurrentPage(1);
    }
  }

  function prevPage() {
    if (currentPage > 1) {
      setCurrentPage(currentPage - 1);
    }
  }

  function nextPage() {
    if (currentPage < Math.ceil(materias.length / materiasPerPage)) {
      setCurrentPage(currentPage + 1);
    }
  }

  function lastPage() {
    if (currentPage < Math.ceil(materias.length / materiasPerPage)) {
      setCurrentPage(Math.ceil(materias.length / materiasPerPage));
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
          menssagem={"Matéria Excluída com SUCESSO!"}
          type={"danger"}
        />
      </div>
      <Card className={"border border-dark bg-dark text-white"}>
        <Card.Header>
          <FontAwesomeIcon icon={faList} />
          {"  "}Lista de Matérias
        </Card.Header>
        <Card.Body>
          <Table bordered hover striped variant="dark">
            <thead>
              <tr>
                <th>Id</th>
                <th>Descrição</th>
                <th>Ações</th>
              </tr>
            </thead>
            <tbody>
              {materias.length === 0 ? (
                <tr align="center">
                  <td colSpan="3">Sem Matérias</td>
                </tr>
              ) : (
                currentMaterias.map((materia) => (
                  <tr key={materia.id}>
                    <td>{materia.id}</td>
                    <td>{materia.descricao}</td>
                    <td>
                      <ButtonGroup
                        style={{
                          display: "flex",
                          justifyContent: "space-between",
                        }}
                      >
                        <Link
                          to={"/materia/form/" + materia.id}
                          className="btn btn-md btn-outline-primary"
                        >
                          <FontAwesomeIcon icon={faEdit} />
                        </Link>
                        <Button
                          size="md"
                          variant="outline-danger"
                          onClick={() => deleteMateria(materia.id)}
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
          <Link to={"/materia/form"} className="nav-link">
            <Button block>
              <FontAwesomeIcon icon={faPlusCircle} /> Nova Matéria
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

export default Materia;
