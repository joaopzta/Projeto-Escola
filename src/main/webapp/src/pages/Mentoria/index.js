import {
  faEdit,
  faList,
  faPlusCircle,
  faTrash,
  faStepForward,
  faStepBackward,
  faFastForward,
  faFastBackward,
} from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { useEffect, useState } from "react";
import {
  FormControl,
  InputGroup,
  Button,
  ButtonGroup,
  Card,
  Table,
} from "react-bootstrap";
import { Link } from "react-router-dom";
import api from "../../services/api";
import MyToast from "../../components/MyToast";
import TablePagination from "../../components/TablePagination";

function Mentoria() {
  const [mentorias, setMentorias] = useState([]);

  const [currentPage, setCurrentPage] = useState(1);
  const [mentoriasPerPage, setMentoriasPerPage] = useState(5);
  const [totalPages, setTotalPages] = useState("");
  const [totalElements, setTotalElements] = useState("");
  const [show, setShow] = useState(false);

  useEffect(() => findAllMentorias(currentPage), [currentPage]);

  async function findAllMentorias(currentPage) {
    currentPage -= 1;
    const response = await api.get(
      `mentoria?page=${currentPage}&size=${mentoriasPerPage}`
    );
    if (response.data != null) {
      setMentorias(response.data.content);
      setTotalPages(response.data.totalPages);
      setTotalElements(response.data.totalElements);
      setCurrentPage(response.data.number + 1);
    }
  }

  async function deleteMentoria(id) {
    try {
      const response = await api.delete(`mentoria/${id}`);
      if (response.data != null) {
        setMentorias(mentorias.filter((mentoria) => mentoria.id !== id));
        setShow(true);
        setTimeout(() => setShow(false), 3000);
      } else {
        setShow(false);
      }
    } catch (err) {
      console.log(err.message);
    }
  }

  function changePage(event) {
    let targetPage = parseInt(event.target.value || "1");
    findAllMentorias(targetPage);
    setCurrentPage(targetPage);
  }

  function firstPage() {
    let firstPage = 1;
    if (currentPage > firstPage) {
      findAllMentorias(firstPage);
    }
  }

  function prevPage() {
    let prevPage = 1;
    if (currentPage > prevPage) {
      findAllMentorias(currentPage - prevPage);
    }
  }

  function nextPage() {
    if (currentPage < Math.ceil(totalElements / mentoriasPerPage)) {
      findAllMentorias(currentPage + 1);
    }
  }

  function lastPage() {
    let condition = Math.ceil(totalElements / mentoriasPerPage);
    if (currentPage < condition) {
      findAllMentorias(condition);
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
          menssagem={"Mentoria Excluída com SUCESSO!"}
          type={"danger"}
        />
      </div>
      <div>
        <Card className={"border border-dark bg-dark text-white"}>
          <Card.Header>
            <FontAwesomeIcon icon={faList} />
            {"   "} Lista de Mentorias
          </Card.Header>
          <Card.Body>
            <Table bordered hover striped variant="dark">
              <thead>
                <tr>
                  <th>Aluno</th>
                  <th>Classe</th>
                  <th>Programa</th>
                  <th></th>
                  <th>Mentor</th>
                  <th>Pais</th>
                  <th>Ações</th>
                </tr>
              </thead>
              <tbody>
                {mentorias.map((mentoria) => (
                  <tr key={mentoria.id}>
                    <td>{mentoria.aluno.nome}</td>
                    <td>{mentoria.aluno.classe}</td>
                    <td>{mentoria.aluno.programa.nome}</td>
                    <td></td>
                    <td>{mentoria.mentor.nome}</td>
                    <td>{mentoria.mentor.pais}</td>
                    <td>
                      <ButtonGroup
                        style={{
                          display: "flex",
                          justifyContent: "space-between",
                        }}
                      >
                        <Link
                          to={`/mentoria/form/${mentoria.id}`}
                          className="btn btn-sm btn-outline-primary"
                        >
                          <FontAwesomeIcon icon={faEdit} />
                        </Link>
                        <Button
                          size="sm"
                          variant="outline-danger"
                          onClick={() => deleteMentoria(mentoria.id)}
                        >
                          <FontAwesomeIcon icon={faTrash} />
                        </Button>
                      </ButtonGroup>
                    </td>
                  </tr>
                ))}
              </tbody>
            </Table>
            <Link to={"/mentoria/form"}>
              <Button size="md" variant="primary" block>
                <FontAwesomeIcon icon={faPlusCircle} />
                {"   "}Nova Mentoria
              </Button>
            </Link>
          </Card.Body>
          <Card.Footer>
          <TablePagination
          currentPage={currentPage}
          itensPerPage={mentoriasPerPage}
          totalElements={totalElements}
          totalPages={totalPages}
          findAllItens={findAllMentorias}
        />
          </Card.Footer>
        </Card>
      </div>
    </div>
  );
}

export default Mentoria;
