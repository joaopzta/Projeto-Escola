import { useState, useEffect } from "react";
import {
  faSave,
  faList,
  faUndo,
  faPlusSquare,
} from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  Form,
  FormGroup,
  Button,
  Card,
  Table,
  Col,
} from "react-bootstrap";
import api from "../../services/api";
import MyToast from "../../components/MyToast";
import TablePagination from "../../components/TablePagination";

function MentoriaForm(props) {
  const [id, setId] = useState(null);
  const [alunos, setAlunos] = useState([]);
  const [alunoSelected, setAlunoSelected] = useState(null);
  const [mentores, setMentores] = useState([]);
  const [mentorSelected, setMentorSelected] = useState(null);
  const [alunoCurrentPage, setAlunoCurrentPage] = useState(1);
  const [mentorCurrentPage, setMentorCurrentPage] = useState(1);
  const [maxPerPage] = useState(5);
  const [alunoTotalPages, setAlunoTotalPages] = useState(null);
  const [alunoTotalElements, setAlunoTotalElements] = useState(null);
  const [mentorTotalPages, setMentorTotalPages] = useState(null);
  const [mentorTotalElements, setMentorTotalElements] = useState(null);

  const [show, setShow] = useState(false);
  const [metodo, setMetodo] = useState("");

  useEffect(() => {
    const mentoriaId = props.match.params.id;
    if (mentoriaId) {
      findMentoriaById(mentoriaId);
    }
  }, [props.match.params.id]);

  function findMentoriaById(id) {
    api
      .get(`mentoria/${id}`)
      .then((response) => {
        if (response.data !== null) {
          setId(response.data.id);
          setAlunoSelected(response.data.aluno);
          setMentorSelected(response.data.mentor);
        }
      })
      .catch((erro) => {
        console.log("Erro: " + erro.message);
      });
  }

  useEffect(() => findAllAlunos(alunoCurrentPage), [alunoCurrentPage]);

  function findAllAlunos(alunoCurrentPage) {
    alunoCurrentPage -= 1;
    api
      .get(`aluno?page=${alunoCurrentPage}&size=${maxPerPage}`)
      .then((response) => response.data)
      .then((data) => {
        setAlunos(data.content);
        setAlunoTotalPages(data.totalPages);
        setAlunoTotalElements(data.totalElements);
        setAlunoCurrentPage(data.number + 1);
      });
  }

  useEffect(() => findAllMentores(mentorCurrentPage), [mentorCurrentPage]);

  function findAllMentores(mentorCurrentPage) {
    mentorCurrentPage -= 1;
    api
      .get(`mentor?page=${mentorCurrentPage}&size=${maxPerPage}`)
      .then((response) => response.data)
      .then((data) => {
        setMentores(data.content);
        setMentorTotalPages(data.totalPages);
        setMentorTotalElements(data.totalElements);
        setMentorCurrentPage(data.number + 1);
      });
  }

  async function submitForm(e) {
    e.preventDefault();
    const novaMentoria = {
      aluno: {
        matricula: alunoSelected.matricula,
      },
      mentor: {
        id: mentorSelected.id,
      },
    };
    try {
      const response = await api.post("mentoria", novaMentoria);
      if (response.data != null) {
        setAlunoSelected("");
        setMentorSelected("");
        setShow(true);
        setMetodo("post");
        setTimeout(() => setShow(false), 3000);
      } else {
        setShow(false);
      }
    } catch (error) {
      console.error(error.message);
    }
  }

  async function updateForm(e) {
    e.preventDefault();
    const updatedMentoria = {
      aluno: {
        matricula: alunoSelected.matricula,
      },
      mentor: {
        id: mentorSelected.id,
      },
    };
    try {
      const response = await api.put(`mentoria/${id}`, updatedMentoria);
      if (response.data != null) {
        setId("");
        setShow(true);
        setMetodo("put");
        setTimeout(() => setShow(false), 3000);
        setTimeout(() => props.history.push("/mentoria"), 3000);
      }
    } catch (err) {
      console.log(err.message);
    }
  }

  return (
    <div>
      <div style={{ display: show ? "block" : "none" }}>
        <MyToast
          show={show}
          menssagem={
            metodo === "put"
              ? "Mentoria Atualizada com SUCESSO!"
              : "Mentoria Salva com SUCESSO!"
          }
          type={"success"}
        />
      </div>
      <Card className={"border border-dark bg-dark text-white"}>
        <Card.Header>
          <FontAwesomeIcon icon={faPlusSquare} /> Nova Mentoria
        </Card.Header>
        <Form onSubmit={id ? updateForm : submitForm} id="MateriaFormId">
          <Card.Body>
            <Form.Row>
              <FormGroup as={Col}>
                <Form.Label>Aluno</Form.Label>
                <Form.Control
                  required
                  value={alunoSelected ? alunoSelected.nome : ""}
                  type="text"
                  readOnly
                />
              </FormGroup>
              <FormGroup as={Col}>
                <Form.Label>Mentor</Form.Label>
                <Form.Control
                  required
                  value={mentorSelected ? mentorSelected.nome : ""}
                  type="text"
                  readOnly
                />
              </FormGroup>
            </Form.Row>
          </Card.Body>
          <Card.Footer style={{ textAlign: "right" }}>
            <Button variant="success" type="submit">
              <FontAwesomeIcon icon={faSave} /> {id ? "Update" : "Save"}
            </Button>
            {"   "}
            <Button
              variant="info"
              disabled={id ? true : false}
              onClick={() => {
                setAlunoSelected("");
                setMentorSelected("");
              }}
            >
              <FontAwesomeIcon icon={faUndo} /> Reset
            </Button>
          </Card.Footer>
        </Form>
      </Card>
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
              </tr>
            </thead>
            <tbody>
              {alunos.length === 0 ? (
                <tr align="center">
                  <td colSpan="5">Sem Alunos</td>
                </tr>
              ) : (
                alunos.map((aluno) => (
                  <tr
                    key={aluno.matricula}
                    onClick={() => setAlunoSelected({ ...aluno })}
                  >
                    <td>{aluno.matricula}</td>
                    <td>{aluno.nome}</td>
                    <td>{aluno.classe}</td>
                    <td>{aluno.programa.nome}</td>
                  </tr>
                ))
              )}
            </tbody>
          </Table>
        </Card.Body>
        <Card.Footer>
          <TablePagination
            currentPage={alunoCurrentPage}
            itensPerPage={maxPerPage}
            totalElements={alunoTotalElements}
            totalPages={alunoTotalPages}
            findAllItens={findAllAlunos}
          />
        </Card.Footer>
      </Card>
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
              </tr>
            </thead>
            <tbody>
              {mentores.length === 0 ? (
                <tr align="center">
                  <td colSpan="4">Sem Mentores</td>
                </tr>
              ) : (
                mentores.map((mentor) => (
                  <tr
                    key={mentor.id}
                    onClick={() => setMentorSelected({ ...mentor })}
                  >
                    <td>{mentor.id}</td>
                    <td>{mentor.nome}</td>
                    <td>{mentor.pais}</td>
                  </tr>
                ))
              )}
            </tbody>
          </Table>
        </Card.Body>
        <Card.Footer>
          <TablePagination
            currentPage={mentorCurrentPage}
            itensPerPage={maxPerPage}
            totalElements={mentorTotalElements}
            totalPages={mentorTotalPages}
            findAllItens={findAllMentores}
          />
        </Card.Footer>
      </Card>
    </div>
  );
}

export default MentoriaForm;
