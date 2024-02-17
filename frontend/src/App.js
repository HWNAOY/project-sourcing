import '@progress/kendo-theme-default/dist/all.css';
import './App.css';
import Home from './components/Home';
import { Route, BrowserRouter as Router, Routes } from 'react-router-dom';
import FinalOutput from './components/FinalOutput';

function App() {
  return (
    <div className="App">
      <Router>
        <Routes>
          <Route exact path='/' element={<Home/>}/>
          <Route exact path='/result' element={<FinalOutput/>}/>
        </Routes>
      </Router>
      
    </div>
  );
}

export default App;
