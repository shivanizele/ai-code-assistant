
import React, { useState } from "react";
import axios from "axios";

function App() {
  const [prompt, setPrompt] = useState("");
  const [response, setResponse] = useState("");
  const [loading, setLoading] = useState(false);

  const generateCode = async () => {
    setLoading(true);
    const finalPrompt = `Write the code in Java language only.\n\n${prompt}`;
    try {
      const res = await axios.post("http://localhost:8080/api/code/generate", 
  { prompt: finalPrompt }, // ✅ wrap the prompt in an object
  { headers: { "Content-Type": "application/json" } }
);
      

      
      setResponse(res.data);
    } catch (error) {
      setResponse("Error generating code.");
    }
    setLoading(false);
  };

  return (
    <div className="min-h-screen bg-gray-100 p-6">
      <div className="max-w-3xl mx-auto bg-white p-8 rounded-2xl shadow-md">
        <h1 className="text-2xl font-bold mb-4 text-center">AI Code Assistant</h1>
        <textarea
          className="w-full h-40 p-3 border rounded mb-4"
          placeholder="Describe your coding problem..."
          value={prompt}
          onChange={(e) => setPrompt(e.target.value)}
        ></textarea>
        <button
          onClick={generateCode}
          disabled={loading}
          className="bg-blue-600 text-white px-6 py-2 rounded hover:bg-blue-700 disabled:opacity-50"
        >
          {loading ? "Generating..." : "Generate Code"}
        </button>
        {response && (
          <pre className="mt-6 bg-gray-900 text-green-400 p-4 rounded overflow-x-auto">
            {response}
          </pre>
        )}
      </div>
    </div>
  );
}

export default App;
