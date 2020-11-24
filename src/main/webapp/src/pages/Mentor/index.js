import React, { useEffect, useState } from "react";
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

function Mentor() {
  const [mentores, setMentores] = useState([]);
  const [currentPage, setCurrentPage] = useState(1);
  const [mentoresPerPage] = useState(5);
  const [show, setShow] = useState(false);

  const lastIndex = currentPage * mentoresPerPage;
  const firstIndex = lastIndex - mentoresPerPage;
  const currentMentores = mentores.slice(firstIndex, lastIndex);
  const totalPages = mentores.length / mentoresPerPage;

  useEffect(findAllProgramas, []);

  function findAllProgramas() {
    api
      .get("mentor")
      .then((response) => response.data)
      .then((data) => {
        setMentores(data);
      });
  }

  function deleteMentor(id) {
    api.delete(`mentor/${id}`).then((response) => {
      if (response.data !== null) {
        setMentores(mentores.filter((mentor) => mentor.id !== id));
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
    if (currentPage < Math.ceil(mentores.length / mentoresPerPage)) {
      setCurrentPage(currentPage + 1);
    }
  }

  function lastPage() {
    if (currentPage < Math.ceil(mentores.length / mentoresPerPage)) {
      setCurrentPage(Math.ceil(mentores.length / mentoresPerPage));
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
          menssagem={"Aluno Excluído com SUCESSO!"}
          type={"danger"}
        />
      </div>
      <Card className={"border border-dark bg-dark text-white"}>
        <Card.Header>
          <FontAwesomeIcon icon={faList} />
          {"  "}Lista de Mentores
        </Card.Header>
        <Card.Body>
          <Table bordered hover striped variant="dark">
            <thead>
              <tr>
                <th>Id</th>
                <th>Nome</th>
                <th>País</th>
                <th>Ações</th>
              </tr>
            </thead>
            <tbody>
              {mentores.length === 0 ? (
                <tr align="center">
                  <td colSpan="4">Sem Mentores</td>
                </tr>
              ) : (
                currentMentores.map((mentor) => (
                  <tr key={mentor.id}>
                    <td>{mentor.id}</td>
                    <td>{mentor.nome}</td>
                    <td>{mentor.pais}</td>
                    <td>
                      <ButtonGroup
                        style={{
                          display: "flex",
                          justifyContent: "space-between",
                        }}
                      >
                        <Link
                          to={"/mentor/form/" + mentor.id}
                          className="btn btn-md btn-outline-primary"
                        >
                          <FontAwesomeIcon icon={faEdit} />
                        </Link>
                        <Button
                          size="md"
                          variant="outline-danger"
                          onClick={() => deleteMentor(mentor.id)}
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
          <Link to={"/mentor/form"} className="nav-link">
            <Button block>
              <FontAwesomeIcon icon={faPlusCircle} /> Novo Mentor
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

export default Mentor;
