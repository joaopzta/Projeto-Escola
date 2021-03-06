import React, { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import MyToast from "../../components/MyToast";
import { Button, Card, Table, ButtonGroup } from "react-bootstrap";
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

function Aluno() {
  const [alunos, setAlunos] = useState([]);
  const [currentPage, setCurrentPage] = useState(1);
  const [alunosPerPage] = useState(5);
  const [show, setShow] = useState(false);
  const [totalPages, setTotalPages] = useState(null);
  const [totalElements, setTotalElements] = useState(null);

  useEffect(() => findAllAlunos(currentPage), [alunosPerPage]);

  function findAllAlunos(currentPage) {
    currentPage -= 1;
    api
      .get(`aluno?page=${currentPage}&size=${alunosPerPage}`)
      .then((response) => response.data)
      .then((data) => {
        setAlunos(data.content);
        setTotalPages(data.totalPages);
        setTotalElements(data.totalElements);
        setCurrentPage(data.number + 1);
      });
  }

  async function deleteAluno(id) {
    const response = await api.delete(`aluno/${id}`);
    if (response.data !== null) {
      setAlunos(alunos.filter((aluno) => aluno.matricula !== id));
      setShow(true);
      setTimeout(() => setShow(false), 3000);
    } else {
      setShow(false);
    }
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
          {"  "}Lista de Alunos
        </Card.Header>
        <Card.Body>
          <Table bordered hover striped variant="dark">
            <thead>
              <tr>
                <th>Matrícula</th>
                <th>Nome</th>
                <th>Classe</th>
                <th>Programa</th>
                <th>Ações</th>
              </tr>
            </thead>
            <tbody>
              {alunos.length === 0 ? (
                <tr align="center">
                  <td colSpan="5">Sem Alunos</td>
                </tr>
              ) : (
                alunos.map((aluno) => (
                  <tr key={aluno.matricula}>
                    <td>{aluno.matricula}</td>
                    <td>{aluno.nome}</td>
                    <td>{aluno.classe}</td>
                    <td>{aluno.programa.nome}</td>
                    <td>
                      <ButtonGroup
                        style={{
                          display: "flex",
                          justifyContent: "space-between",
                        }}
                      >
                        <Link
                          to={"/aluno/form/" + aluno.matricula}
                          className="btn btn-md btn-outline-primary"
                        >
                          <FontAwesomeIcon icon={faEdit} />
                        </Link>
                        <Button
                          size="md"
                          variant="outline-danger"
                          onClick={() => deleteAluno(aluno.matricula)}
                        >
                          <FontAwesomeIcon icon={faTrash} />
                        </Button>
                        <Link
                          to={`/aluno/${aluno.matricula}/mentoria/`}
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
          <Link to={"/aluno/form"} className="nav-link">
            <Button block>
              <FontAwesomeIcon icon={faPlusCircle} /> Novo Aluno
            </Button>
          </Link>
        </Card.Body>
        <Card.Footer>
          <TablePagination
            currentPage={currentPage}
            itensPerPage={alunosPerPage}
            totalElements={totalElements}
            totalPages={totalPages}
            findAllItens={findAllAlunos}
          />
        </Card.Footer>
      </Card>
    </div>
  );
}

export default Aluno;
