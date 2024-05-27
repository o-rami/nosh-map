import { useLocation } from "react-router-dom"

export default function NotFound() {
  const location = useLocation();

  return <div style= {{ padding:20 }} className="container-fluid">
    NOT FOUND. {location.state && location.state.msg}
  </div>
}
