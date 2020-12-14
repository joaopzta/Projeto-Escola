import React, { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import MyToast from "../../components/MyToast";
import {
  Button,
  Card,
  Table,
  ButtonGroup,
} from "react-bootstrap";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faEdit,
  faList,
  faTrash,
  faPlusCircle,
  faChalkboardTeacher,
} from "@fortawesome/free-solid-svg-icons";
import api from "../../services/api";
import TablePagination from "../../components/TablePagination";

function Mentor() {
  const [mentores, setMentores] = useState([]);
  const [currentPage, setCurrentPage] = useState(1);
  const [mentoresPerPage] = useState(5);
  const [totalElements, setTotalElements] = useState(null);
  const [totalPages, setTotalPages] = useState(null);
  const [show, setShow] = useState(false);

  useEffect(() => findAllMentores(currentPage), [mentoresPerPage]);

  function findAllMentores(currentPage) {
    currentPage -= 1;
    api
      .get(`mentor?page=${currentPage}&size=${mentoresPerPage}`)
      .then((response) => response.data)
      .then((data) => {
        setMentores(data.content);
        setTotalPages(data.totalPages);
        setTotalElements(data.totalElements);
        setCurrentPage(currentPage + 1);
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
                mentores.map((mentor) => (
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
                        <Link
                          to={`/mentor/${mentor.id}/mentoria`}
                          className="btn btn-md btn-outline-info"
                        >
                          <FontAwesomeIcon icon={faChalkboardTeacher} />
                        </Link>
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
          <TablePagination
            currentPage={currentPage}
            itensPerPage={mentoresPerPage}
            totalElements={totalElements}
            totalPages={totalPages}
            findAllItens={findAllMentores}
          />
        </Card.Footer>
      </Card>
    </div>
  );
}

export default Mentor;
