import axios from "axios";
import React, { useEffect, useRef, useState } from "react";
import { useNavigate } from "react-router-dom";
import "../styling/Home.css";
import { ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import "../styling/file-upload.styles";
import {
  DragDropText,
  FileMetaData,
  FilePreviewContainer,
  FileUploadContainer,
  FormField,
  ImagePreview,
  PreviewContainer,
  RemoveFileIcon,
  UploadFileBtn,
} from "../styling/file-upload.styles";

function Home() {
  let navigate = useNavigate();
  const [file, setfile] = useState(null);
  const [isImageFile, setisimagefile] = useState(null);
  const [processIds, setprocessId] = useState([]);
  const [pid, setpid] = useState(1);
  const [pidObj, setpidObj] = useState({ id: 1 });
  const fileInputField = useRef(null);

  const [uploadProgress, setUploadProgress] = useState(0);

  useEffect(() => {
    loadProcess();
  }, []);

  const loadProcess = async () => {
    const newprocessIds = await axios.get("http://localhost:8080/processId");
    setprocessId(newprocessIds.data);
    const selectedProcess = newprocessIds.data.find((item) => item.id === 1);
    // console.log(selectedProcess);
    setpidObj(selectedProcess);
  };

  const handleFileSelect = (e) => {
    // let newfile = e.target.files[0];
    const { files: newfiles } = e.target;
    let newfile = newfiles[0];
    // console.log(newfile);
    const newisImageFile = newfile.type.split("/")[0] === "image";
    setisimagefile(newisImageFile);
    // console.log(newfile);
    setfile(newfile);
  };

  const handleUpload = async (e) => {
    e.preventDefault();

    let formData = new FormData();
    formData.append("file", file);
    try {
      await axios.post("http://localhost:8080/upload", formData, {
        headers: {
          "Content-Type": "multipart/form-data",
        },
      });

      const newprocessIds = await axios.get("http://localhost:8080/processId");
      setprocessId(newprocessIds.data);
      if (newprocessIds.data.length > 0) {
        toast.success(
          `File uploaded successfully and process id ${
            newprocessIds.data[newprocessIds.data.length - 1].id
          } added`
        );
      } else {
        toast.success("File uploaded successfully and process id 1 added");
      }
      console.log("File uploaded successfully");
    } catch (error) {
      toast.error("File upload failed");
      console.log("File upload failed", error);
    }
    const xhr = new XMLHttpRequest();
    xhr.upload.addEventListener("progress", (event) => {
      if (event.lengthComputable) {
        const percentComplete = (event.loaded / event.total) * 100;
        setUploadProgress(percentComplete);
      }
    });

    xhr.open("POST", "http://localhost:8080/upload", true);
    xhr.send(formData);
  };

  const handleFileBtn = (e) => {
    e.preventDefault();
    fileInputField.current.click();
  };
  const removeFile = () => {
    setfile(null);
  };

  const handleResult = (e) => {
    e.preventDefault();
    navigate("/result", { state: pidObj });
  };
  const handleDdown = (e) => {
    const selectedId = e.target.value;
    setpid(selectedId);
    const selectedProcess = processIds.find((item) => item.id == selectedId);
    // console.log(selectedProcess);
    setpidObj(selectedProcess);
  };

  const handleFileDrop = (e) => {
    e.preventDefault();
    const newfile = e.dataTransfer.files?.[0];
    // console.log(newfile);
    const newisImageFile = newfile.type.split("/")[0] === "image";
    setisimagefile(newisImageFile);
    // console.log(newfile);
    setfile(newfile);
  };

  const DEFAULT_MAX_FILE_SIZE_IN_BYTES = 500000;
  const KILO_BYTES_PER_BYTE = 1000;

  const convertBytesToKB = (bytes) => Math.round(bytes / KILO_BYTES_PER_BYTE);

  return (
    <div className="container">
      <ToastContainer />

      <form className="upload-form">
        <h1>Upload RFQ</h1>

        <FileUploadContainer
          onDrop={handleFileDrop}
          onDragOver={(e) => e.preventDefault()}
        >
          <DragDropText>Drag and drop your RFQs anywhere or</DragDropText>
          <UploadFileBtn type="button" onClick={handleFileBtn}>
            <i className="fas fa-file-upload" />
            <span> Upload an RFQ</span>
          </UploadFileBtn>
          <FormField
            type="file"
            name="file"
            title=""
            value=""
            ref={fileInputField}
            onChange={handleFileSelect}
          />
        </FileUploadContainer>
        <FilePreviewContainer>
          {file && (
            <PreviewContainer>
              <div>
                {isImageFile && (
                  <ImagePreview
                    src={URL.createObjectURL(file)}
                    alt={`file preview`}
                  />
                )}
                <FileMetaData isImageFile={isImageFile}>
                  <span>{file.name}</span>
                  <aside>
                    <span>{convertBytesToKB(file.size)} kb</span>
                    <RemoveFileIcon
                      className="fas fa-trash-alt"
                      onClick={() => removeFile()}
                    />
                  </aside>
                </FileMetaData>
              </div>
            </PreviewContainer>
          )}
        </FilePreviewContainer>
        
        {/* {uploadProgress > 0 && (
          <div>
            <p>Upload Progress: {uploadProgress.toFixed(2)}%</p>
          </div>
        )} */}
        <button className="upload-button" onClick={handleUpload}>
          Submit
        </button>

        {/* <label className="label-for-file">
          Choose RFQ
          <input
            type="file"
            name="file"
            accept=".xlsx, .xls"
            onChange={handleFileSelect}
          />
        </label>
        <div className="selected-file-name">
          {file ? file.name : "No RFQ selected"}
        </div>
        <button className="upload-button" onClick={handleUpload}>
          Submit
        </button> */}
        <br />
        <br />
        <select value={pid} onChange={handleDdown} className="dropdown">
          {processIds.map((item, index) => (
            <option key={index} value={item.id}>
              Select File with Process Id: {item.id}
            </option>
          ))}
        </select>
        <button className="result-button" onClick={handleResult}>
          Optimize Results
        </button>
      </form>
    </div>
  );
}

export default Home;
