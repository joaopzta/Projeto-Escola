import React, { useState, useEffect } from "react";
import { Card, Col, Form, Table } from "react-bootstrap";
import api from "../../services/api";

function Mentoria(props) {
  const [aluno, setAluno] = useState({});
  const [alunoPrograma, setAlunoPrograma] = useState({});
  const [mentorias, setMentorias] = useState([]);
  const [listaNotas, setListaNotas] = useState([]);

  const alunoId = props.match.params.id;

  useEffect(() => {
    if (alunoId) {
      findAlunoById(alunoId);
    }
  }, [alunoId]);

  useEffect(() => {
    findAlunoMentorias();
  }, [aluno]);

  async function findAlunoById(id) {
    const response = await api.get(`aluno/${id}`);
    if (response.data !== null) {
      setAluno(response.data);
      setAlunoPrograma(response.data.programa);
      setListaNotas(response.data.listaNotaAluno);
    }
  }

  async function findAlunoMentorias() {
    if (aluno.matricula != null) {
      const response = await api.get("mentoria");
      const data = await response.data;
      const mentoriasAluno = data.content.filter(
        (mentoria) => mentoria.aluno.matricula === aluno.matricula
      );
      setMentorias(mentoriasAluno);
    }
  }

  return (
    <div>
      <Card className={"border border-dark bg-dark text-white"}>
        <Card.Header>Aluno</Card.Header>
        <Card.Body>
          <Form>
            <Form.Row>
              <Form.Group as={Col}>
                <Form.Label>Matricula</Form.Label>
                <Form.Control
                  value={aluno.matricula}
                  type="text"
                  name="matricula"
                  disabled
                />
              </Form.Group>
              <Form.Group as={Col}>
                <Form.Label>Nome</Form.Label>
                <Form.Control
                  value={aluno.nome}
                  type="text"
                  name="nome"
                  disabled
                />
              </Form.Group>
            </Form.Row>
            <Form.Row>
              <Form.Group as={Col}>
                <Form.Label>Classe</Form.Label>
                <Form.Control
                  value={aluno.classe}
                  type="text"
                  name="classe"
                  disabled
                />
              </Form.Group>
              <Form.Group as={Col}>
                <Form.Label>Programa</Form.Label>
                <Form.Control
                  value={alunoPrograma.nome}
                  type="text"
                  name="programa"
                  disabled
                />
              </Form.Group>
            </Form.Row>
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
                <th>Mentor</th>
                <th>País</th>
              </tr>
            </thead>
            <tbody>
              {mentorias.length === 0 ? (
                <tr align="center">
                  <td colSpan="2">Sem Mentorias</td>
                </tr>
              ) : (
                mentorias.map((mentoria) => (
                  <tr key={mentoria.id}>
                    <td>{mentoria.mentor.nome}</td>
                    <td>{mentoria.mentor.pais}</td>
                  </tr>
                ))
              )}
            </tbody>
          </Table>
        </Card.Body>
      </Card>
      <Card
        className={"border border-dark bg-dark text-white"}
        style={{ marginTop: "20px" }}
      >
        <Card.Header className="text-center">Notas do Aluno</Card.Header>
        <Card.Body>
          <Table borderless hover striped variant="dark" size="sm">
            <tbody>
              {listaNotas.length === 0 ? (
                <tr align="center">
                  <td colSpan="3">Sem Notas</td>
                </tr>
              ) : (
                listaNotas.map((nota) => (
                  <tr key={nota.id}>
                    <td>{nota.materia.descricao}</td>
                    <td>{nota.nota}</td>
                    <td align="center">{nota.dataNota}</td>
                  </tr>
                ))
              )}
            </tbody>
          </Table>
        </Card.Body>
      </Card>
    </div>
  );
}

export default Mentoria;
