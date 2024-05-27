import '../index.css';
import { useNavigate } from "react-router-dom";

function Errors({ msg }) {

  const navigate = useNavigate();

  return (
    <>
      {(navigate?.location?.state?.msg || msg) && <p> Error: {" "}{msg} </p>}
      </>
  );
}

export default Errors;
