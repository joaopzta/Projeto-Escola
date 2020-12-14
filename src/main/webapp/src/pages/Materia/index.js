import React, { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import MyToast from "../../components/MyToast";
import { Button, Card, Table, ButtonGroup } from "react-bootstrap";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faEdit,
  faList,
  faTrash,
  faPlusCircle,
} from "@fortawesome/free-solid-svg-icons";
import api from "../../services/api";
import TablePagination from "../../components/TablePagination";

function Materia() {
  const [materias, setMaterias] = useState([]);
  const [currentPage, setCurrentPage] = useState(1);
  const [materiasPerPage] = useState(5);
  const [show, setShow] = useState(false);
  const [totalElements, setTotalElements] = useState(null);
  const [totalPages, setTotalPages] = useState(null);

  useEffect(() => findAllMaterias(currentPage), [materiasPerPage]);

  function findAllMaterias(currentPage) {
    currentPage -= 1;
    api
      .get(`materia?page=${currentPage}&size=${materiasPerPage}`)
      .then((response) => response.data)
      .then((data) => {
        setMaterias(data.content);
        setTotalPages(data.totalPages);
        setTotalElements(data.totalElements);
        setCurrentPage(data.number + 1);
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
                materias.map((materia) => (
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
          <TablePagination
            currentPage={currentPage}
            itensPerPage={materiasPerPage}
            totalElements={totalElements}
            totalPages={totalPages}
            findAllItens={findAllMaterias}
          />
        </Card.Footer>
      </Card>
    </div>
  );
}

export default Materia;
