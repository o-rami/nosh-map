import { useNavigate } from "react-router-dom";

function Confirmation() {
  const navigate = useNavigate();

  return (
    <p>
      CRUD ✅ {history.location.state ? ` - ${history.location.state.msg}` : ""}
    </p>
  );
}

export default Confirmation;
