import React from 'react';
import { Toast, ToastBody } from 'react-bootstrap';

export default function MyToast({ mostrarToast }) {
  const toastCss = {
    position: "fixed",
    top: "10px",
    right: "10px",
    zIndex: "1",
    boxShadow: "0 4px 8px 0 rgba(0,0,0,0.2), 0 6px 20px 0 rgba(0,0,0,0.19)",
  }

  return (
    <div style={mostrarToast.show ? toastCss : null}>
      <Toast className={`border text-white ${mostrarToast.type === "success" ? "border-success bg-success" : "border-danger bg-danger"} `} show={mostrarToast.show}>
        <Toast.Header className={`bg-success text-white ${mostrarToast.type === "success" ? "border-success bg-success" : "border-danger bg-danger"}`} closeButton={false}>
          <strong className="mr-auto">Sucesso</strong>
        </Toast.Header>
        <ToastBody>
          {mostrarToast.menssagem}
        </ToastBody>
      </Toast>
    </div>
  );
}