import { useNavigate } from "react-router-dom";
import "./Signup.css";
import { useState } from "react";
//import axios from "axios";
import api from "../api/axiosConfig";

function Signup() {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [errors, setErrors] = useState({}); // 에러가 백엔드에서 map형태 json형태로 와서 {}로 초기화
  const navigate = useNavigate();

  const handleSignup = async (e) => {
    e.preventDefault(); //submit 이벤트 후 초기화 현상 방지
    setErrors({});

    try {
      await api.post("/api/auth/signup", { username, password });
      alert("회원가입 성공!");
      navigate("/login");
    } catch (err) {
      if (err.response && err.response.status === 400) {
        //회원가입 에러 발생
        setErrors(err.response.data); //에러 추출 -> errors에 저장
      } else {
        console.error("회원가입실패:", err);
        alert("회원가입 실패!");
      }
    }
  };

  return (
    <div className="form-container">
      <h2>회원가입</h2>
      <form onSubmit={handleSignup}>
        <input
          type="text"
          placeholder="아이디"
          value={username}
          onChange={(e) => setUsername(e.target.value)}
        />

        <input
          type="password"
          placeholder="비밀번호"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        />
        {errors.username && <p style={{ color: "red" }}>{errors.username}</p>}
        {/* 에러시 아이디는 최소4글자 이상입니다 화면에 출력 */}
        {errors.password && <p style={{ color: "red" }}>{errors.password}</p>}
        {errors.iderror && <p style={{ color: "red" }}>{errors.iderror}</p>}
        <button type="submit">회원가입</button>
      </form>
    </div>
  );
}

export default Signup;
