import { useState } from 'react'
import reactLogo from './assets/react.svg'
import viteLogo from '/vite.svg'
import './App.css'
import FeeEstimator from './components/FeeEstimator';


function App() {
  return (
    <div className="min-h-screen p-6">
      <h1 className="text-2xl font-bold text-center mb-8">Fee Calculator (Proposed)</h1>
      <FeeEstimator />
    </div>
  );
}

export default App
