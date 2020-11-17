import React from "react";
import { Jumbotron } from "react-bootstrap";

function Home() {
  return (
    <Jumbotron className="bg-dark text-white" style={{ textAlign: "center" }}>
      <h1>Bem Vindo a Escola JOOJ</h1>
      <blockquote className="blockquote mb-0">
        <p>Uma API REST feita com Spring-boot e React, para fins de estudo</p>
        <footer className="blockquote-footer">João Pedro</footer>
      </blockquote>
    </Jumbotron>
  );
}

export default Home;
