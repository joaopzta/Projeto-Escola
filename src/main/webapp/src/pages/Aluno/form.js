import React, { useEffect, useState } from "react";
import { Button, Card, Form, Col } from "react-bootstrap";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faEdit,
  faPlusSquare,
  faSave,
  faUndo,
} from "@fortawesome/free-solid-svg-icons";
import MyToast from "../../components/MyToast";
import api from "../../services/api";

function AlunoForm(props) {
  const [matricula, setMatricula] = useState("");
  const [nome, setNome] = useState("");
  const [classe, setClasse] = useState("");
  const [programas, setProgramas] = useState([]);
  const [selectedPrograma, setSelectedPrograma] = useState("");
  const [listaDeNotas, setListaDeNotas] = useState([]);
  const [show, setShow] = useState(false);
  const [metodo, setMetodo] = useState("");

  useEffect(() => {
    const alunoId = props.match.params.id;
    if (alunoId) {
      findAlunoById(alunoId);
    }
    api
      .get("programa")
      .then((response) => response.data)
      .then((data) => {
        setProgramas(data);
      });
  }, [props.match.params.id]);

  function findAlunoById(id) {
    api
      .get(`aluno/${id}`)
      .then((response) => {
        if (response.data !== null) {
          setMatricula(response.data.matricula);
          setNome(response.data.nome);
          setClasse(response.data.classe);
          setSelectedPrograma(response.data.programa);
          setListaDeNotas(response.data.listaDeNotas);
        }
      })
      .catch((erro) => {
        console.log("Erro: " + erro);
      });
  }

  function submitForm(event) {
    event.preventDefault();
    const aluno = {
      nome: nome,
      classe: classe,
      programa: { id: selectedPrograma },
      listaDeNotas: listaDeNotas,
    };

    api
      .post("aluno", aluno)
      .then((response) => {
        if (response.data !== null) {
          if (aluno.programa !== null) {
            setNome("");
            setClasse("");
            setShow(true);
            setMetodo("post");
            setTimeout(() => setShow(false), 3000);
          } else {
            console.log(aluno);
          }
        } else {
          setShow(false);
        }
      })
      .catch((erro) => {
        console.log("Erro: " + erro);
      });
  }

  function updateForm(event) {
    event.preventDefault();
    const aluno = {
      nome: nome,
      classe: classe,
      programa: { id: selectedPrograma },
      listaDeNotas: listaDeNotas,
    };
    api.put(`aluno/${matricula}`, aluno).then((response) => {
      if (response.data !== null) {
        setNome("");
        setClasse("");
        setListaDeNotas(null);
        setShow(true);
        setMetodo("put");
        setTimeout(() => setShow(false), 3000);
        setTimeout(() => props.history.push("/aluno"), 3000);
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
          menssagem={
            metodo === "put"
              ? "Aluno Atualizado com SUCESSO!"
              : "Aluno Salvo com SUCESSO!"
          }
          type={"success"}
        />
      </div>
      <Card className={"border border-dark bg-dark text-white"}>
        <Card.Header>
          <FontAwesomeIcon icon={matricula ? faEdit : faPlusSquare} />{" "}
          {matricula ? "Atualizar Aluno" : "Novo Aluno"}
        </Card.Header>
        <Form onSubmit={matricula ? updateForm : submitForm} id="AlunoFormId">
          <Card.Body>
            <Form.Row>
              <Form.Group as={Col}>
                <Form.Label>Nome</Form.Label>
                <Form.Control
                  required
                  value={nome}
                  onChange={(event) => {
                    setNome(event.target.value);
                  }}
                  type="text"
                  name="nome"
                  placeholder="Nome do Aluno"
                />
              </Form.Group>
              <Form.Group as={Col}>
                <Form.Label>Classe</Form.Label>
                <Form.Control
                  required
                  value={classe}
                  onChange={(event) => {
                    setClasse(event.target.value);
                  }}
                  type="text"
                  name="classe"
                  placeholder="Classe"
                />
              </Form.Group>
            </Form.Row>
            <Form.Row>
              <Form.Label>Programa</Form.Label>
              <Form.Control
                as="select"
                size="md"
                onChange={(event) => {
                  setSelectedPrograma(event.target.value);
                }}
                custom
              >
                {programas.map((programa) => (
                  <option key={programa.id} value={programa.id}>
                    {programa.nome}
                  </option>
                ))}
              </Form.Control>
            </Form.Row>
          </Card.Body>
          <Card.Footer style={{ textAlign: "right" }}>
            <Button variant="success" type="submit">
              <FontAwesomeIcon icon={faSave} /> {matricula ? "Update" : "Save"}
            </Button>
            {"  "}
            <Button
              variant="info"
              type="submit"
              onClick={() => {
                setNome("");
                setClasse("");
                setListaDeNotas(null);
              }}
            >
              <FontAwesomeIcon icon={faUndo} /> Reset
            </Button>
          </Card.Footer>
        </Form>
      </Card>
    </div>
  );
}

export default AlunoForm;
