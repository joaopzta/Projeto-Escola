import React, { useEffect, useState } from "react";
import { ButtonGroup, Card, Form, Table, Button, Col } from "react-bootstrap";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faEdit,
  faSave,
  faUndo,
  faTimes,
  faPlusCircle,
  faTrash,
} from "@fortawesome/free-solid-svg-icons";
import api from "../../services/api";

function MentoriaForm(props) {
  const [mentor, setMentor] = useState({});
  const [aluno, setAluno] = useState({});
  const [mentorias, setMentorias] = useState([]);
  const [alunoListaNotas, setAlunoListaNotas] = useState([]);
  const [notaArrayId, setNotaArrayId] = useState(null);
  const [nota, setNota] = useState({});
  const [materias, setMaterias] = useState({});
  const [selectedMateria, setSelectedMateria] = useState("");
  const [isNovaNota, setIsNovaNota] = useState(false);

  const mentorId = props.match.params.id;

  useEffect(() => {
    if (mentorId) {
      findMentorById(mentorId);
    }
  }, [mentorId]);

  useEffect(() => {
    findMentoresMentoria();
    findAllMaterias();
  }, [mentor]);

  async function findMentorById(id) {
    const response = await api.get(`mentor/${id}`);
    if (response.data != null) {
      setMentor(response.data);
    }
  }

  async function findMentoresMentoria() {
    if (mentor.id != null) {
      const response = await api.get("mentoria");
      const data = await response.data;
      const mentoriaMentores = data.content.filter(
        (mentoria) => mentoria.mentor.id === mentor.id
      );
      setMentorias(mentoriaMentores);
    }
  }

  async function getNotasAluno(id) {
    if (id != null) {
      const response = await api.get(`aluno/${id}`);
      setAluno(response.data);
      setAlunoListaNotas(response.data.listaNotaAluno);
    }
  }

  async function findAllMaterias() {
    const response = await api.get("materia");
    setMaterias(response.data.content);
  }

  async function submitForm(event) {
    event.preventDefault();

    const novaNota = {
      nota: nota.nota,
      dataNota: nota.dataNota,
      materia: { id: selectedMateria },
    };

    try {
      const response = await api.put(`aluno/${aluno.matricula}/nota`, novaNota);
      if (response.data !== null) {
        //setNotaArrayId(null);
        setIsNovaNota(false)
        getNotasAluno(aluno.matricula);
      }
    } catch (err) {
      console.error(err);
    }
    
  }

  async function updateForm(event) {
    event.preventDefault();
    const novaNota = {
      nota: nota.nota,
      dataNota: nota.dataNota,
      materia: { id: nota.materia.id },
    };

    try {
      const response = await api.put(`aluno/${aluno.matricula}/nota/${notaArrayId}`, novaNota);
      if (response.data !== null) {
        setNotaArrayId(null);
        getNotasAluno(aluno.matricula);
      }
    } catch (err) {
      console.error(err);
    }
  }

  async function deleteNotaAluno(id) {
    const response = await api.delete(`aluno/${aluno.matricula}/nota/${id}`);
    if (response.data != null) {
      setAlunoListaNotas(alunoListaNotas.filter((nota, index) => index !== id));
    }
  }

  return (
    <div>
      <Card className={"border border-dark bg-dark text-white"}>
        <Card.Header>Mentor</Card.Header>
        <Card.Body>
          <Form>
            <Form.Group>
              <Form.Label>Nome</Form.Label>
              <Form.Control
                type="text"
                value={mentor.nome}
                name="nome"
                disabled
              />
            </Form.Group>
          </Form>
        </Card.Body>
      </Card>
      <Card
        className={"border border-dark bg-dark text-white"}
        style={{ marginTop: "20px" }}
      >
        <Card.Header>Mentorias</Card.Header>
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
              {mentorias.length === 0 ? (
                <tr align="center">
                  <td colSpan="2">Sem Mentorias</td>
                </tr>
              ) : (
                mentorias.map((mentoria) => (
                  <tr
                    key={mentoria.id}
                    onClick={(e) => {
                      notaArrayId != null
                        ? e.preventDefault()
                        : getNotasAluno(mentoria.aluno.matricula);
                    }}
                  >
                    <td>{mentoria.aluno.matricula}</td>
                    <td>{mentoria.aluno.nome}</td>
                    <td>{mentoria.aluno.classe}</td>
                    <td>{mentoria.aluno.programa.nome}</td>
                  </tr>
                ))
              )}
            </tbody>
          </Table>
        </Card.Body>
      </Card>
      {notaArrayId != null ? (
        <Card
          className={"border border-dark bg-dark text-white"}
          style={{ marginTop: "20px" }}
        >
          <Card.Header className="text-center">
            Notas do Aluno: {aluno.nome}
          </Card.Header>
          <Form
            onSubmit={isNovaNota ? submitForm : updateForm}
            id="mentorFormId"
          >
            <Card.Body>
              <Form.Row>
                <Form.Group as={Col}>
                  <Form.Label>Matéria</Form.Label>
                  {isNovaNota ? (
                    <Form.Control
                      as="select"
                      autoFocus
                      onFocus={(e) => {
                        setSelectedMateria(e.target.value);
                      }}
                      onChange={(e) => {
                        setSelectedMateria(e.target.value);
                      }}
                    >
                      {materias.map((materia) => (
                        <option key={materia.id} value={materia.id}>
                          {materia.descricao}
                        </option>
                      ))}
                    </Form.Control>
                  ) : (
                    <Form.Control
                      disabled
                      type="text"
                      value={nota.materia.descricao}
                      name="materia"
                    />
                  )}
                </Form.Group>
                <Form.Group as={Col}>
                  <Form.Label>Nota</Form.Label>
                  <Form.Control
                    type="text"
                    value={nota.nota}
                    name="nota"
                    onChange={(e) => {
                      setNota({ ...nota, nota: e.target.value });
                    }}
                  />
                </Form.Group>
                <Form.Group as={Col}>
                  <Form.Label>Data</Form.Label>
                  <Form.Control
                    type={"date"}
                    value={nota.dataNota}
                    name="dataNota"
                    onChange={(e) => {
                      //setNotaData(e.target.value);
                      setNota({ ...nota, dataNota: e.target.value });
                    }}
                  />
                </Form.Group>
              </Form.Row>
            </Card.Body>
            <Card.Footer style={{ textAlign: "right" }}>
              <Button variant="success" type="submit">
                <FontAwesomeIcon icon={faSave} />{" "}
                {isNovaNota ? "Save" : "Update"}
              </Button>
              {"  "}
              <Button
                variant="warning"
                type="submit"
                onClick={() => {
                  setNotaArrayId(null);
                  setIsNovaNota(false);
                }}
              >
                <FontAwesomeIcon icon={faTimes} /> Cancel
              </Button>
              {"  "}
              <Button
                variant="info"
                type="submit"
                onClick={() => {
                  setNota({});
                }}
              >
                <FontAwesomeIcon icon={faUndo} /> Reset
              </Button>
            </Card.Footer>
          </Form>
        </Card>
      ) : (
        <Card
          className={"border border-dark bg-dark text-white"}
          style={{ marginTop: "20px" }}
        >
          <Card.Header className="text-center">
            Notas do Aluno: {aluno.nome}
          </Card.Header>
          <Card.Body>
            <Table borderless hover striped variant="dark" size="sm">
              <tbody>
                {alunoListaNotas.length === 0 ? (
                  <tr align="center">
                    <td colSpan="3">Sem Notas</td>
                  </tr>
                ) : (
                  alunoListaNotas.map((notaAluno) => (
                    <tr key={notaAluno.id}>
                      <td>{notaAluno.materia.descricao}</td>
                      <td>{notaAluno.nota}</td>
                      <td align="center">{notaAluno.dataNota}</td>
                      <td style={{ display: "flex", justifyContent: "space-around" }}>
                        <ButtonGroup>
                          <Button
                            size="sm"
                            variant="outline-primary"
                            onClick={() => {
                              setNota(notaAluno);
                              setNotaArrayId(
                                alunoListaNotas.indexOf(notaAluno)
                              );
                            }}
                          >
                            <FontAwesomeIcon icon={faEdit} />
                          </Button>
                          <Button
                            size="sm"
                            variant="outline-danger"
                            onClick={() => {
                              deleteNotaAluno(alunoListaNotas.indexOf(notaAluno));
                            }}
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
          </Card.Body>
          <Card.Footer style={{ textAlign: "right" }}>
            <ButtonGroup>
              <Button
                onClick={() => {
                  setIsNovaNota(true);
                  setNotaArrayId(alunoListaNotas.length + 1);
                }}
              >
                <FontAwesomeIcon icon={faPlusCircle} /> Adicionar Nota
              </Button>
            </ButtonGroup>
          </Card.Footer>
        </Card>
      )}
    </div>
  );
}

export default MentoriaForm;
