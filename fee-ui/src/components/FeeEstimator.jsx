import { useEffect, useState } from "react";
import axios from "axios";

export default function FeeEstimator() {
  const [apiMap, setApiMap] = useState({});
  const [selectedService, setSelectedService] = useState("");
  const [selectedApi, setSelectedApi] = useState("");
  const [parameters, setParameters] = useState([]);
  const [values, setValues] = useState({});
  const [feeResult, setFeeResult] = useState(null);

  const backendUrlPrefix = "http://localhost:8080/api/v1/transactions";

  const serviceIcons = {
    Crypto: "https://files.hedera.com/Nav-Icon-HBAR-std.svg?dm=1709012020",
    Consensus: "https://files.hedera.com/Nav-Icon-Consensus-Service-std.svg?dm=1709011923",
    Token: "https://files.hedera.com/Nav-Icon-Token-Service-std.svg?dm=1709012106",
    "Smart Contracts": "https://files.hedera.com/Nav-Icon-Smart-Contracts-std.svg?dm=1709012090",
    File: "https://files.hedera.com/Nav-Icon-Grant-Funding-std.svg?dm=1709012009",
    Miscellaneouse: "https://files.hedera.com/Nav-Icon-Fee-Estimator-std.svg?dm=1709011983",
  };

  useEffect(() => {
    axios.get(`${backendUrlPrefix}`)
      .then(res => setApiMap(res.data))
      .catch(err => console.error("Error loading API map:", err));
  }, []);

  useEffect(() => {
    if (!selectedApi) return;
    axios.get(`${backendUrlPrefix}/${selectedApi}/parameters`)
      .then(res => {
        const defaults = {};
        for (const param of res.data) {
          defaults[param.name] = param.defaultValue;
        }
        setParameters(res.data);
        setValues(defaults);
        setFeeResult(null);
      })
      .catch(err => console.error("Error loading parameters:", err));
  }, [selectedApi]);

  useEffect(() => {
    if (!selectedApi || Object.keys(values).length === 0) return;
    const delayDebounceFn = setTimeout(() => {
      axios.post(`${backendUrlPrefix}/${selectedApi}/fee`, values)
        .then(res => setFeeResult(res.data))
        .catch(err => console.error("Fee computation failed:", err));
    }, 300);

    return () => clearTimeout(delayDebounceFn);
  }, [values, selectedApi]);

  const updateValue = (name, value) => {
    setValues(prev => ({ ...prev, [name]: value }));
  };

  return (
    <div className="min-h-screen bg-[#1c1c1c] text-white font-sans p-6">
      <div className="grid grid-cols-[20%_20%_1fr] gap-8">

        {/* STEP 1 */}
        <div>
          <h2 className="text-sm text-[#8c8c8c] uppercase tracking-widest mb-1 border-b border-indigo-500 pb-1">Step 1</h2>
          <p className="text-lg font-semibold mb-4">Select a <span className="text-white font-bold">Hedera service</span></p>
          <div className="flex flex-col gap-4">
            {Object.keys(apiMap).map(service => (
              <button
                key={service}
                className={`flex items-center gap-3 text-sm px-2 py-1 rounded transition-all duration-150 ${selectedService === service ? 'bg-indigo-700 text-white' : 'hover:bg-[#2a2a2a]'}`}
                onClick={() => {
                  setSelectedService(service);
                  setSelectedApi("");
                }}
              >
                <img src={serviceIcons[service] || ""} alt={service} className="w-6 h-6" />
                <span className="text-left font-medium">{service.toUpperCase()} SERVICE</span>
              </button>
            ))}
          </div>
        </div>

        {/* STEP 2 */}
        <div>
          <h2 className="text-sm text-[#8c8c8c] uppercase tracking-widest mb-1 border-b border-indigo-500 pb-1">Step 2</h2>
          <p className="text-lg font-semibold mb-4">Select a <span className="text-white font-bold">Network API</span></p>
          <div className="flex flex-col gap-2">
            {selectedService && apiMap[selectedService]?.map(api => (
              <button
                key={api}
                className={`text-left px-2 py-1 rounded transition-all duration-150 ${selectedApi === api ? 'bg-indigo-700 text-white' : 'hover:bg-[#2a2a2a]'}`}
                onClick={() => setSelectedApi(api)}
              >
                {api}
              </button>
            ))}
          </div>
        </div>

        {/* STEP 3 */}
        <div>
          <h2 className="text-sm text-[#8c8c8c] uppercase tracking-widest mb-1 border-b border-indigo-500 pb-1">Step 3</h2>
          {selectedApi && (
            <>
              <p className="text-lg font-semibold mb-4">Enter the <span className="text-white font-bold">API call parameters</span> <span className="text-[#aaa]">({selectedApi})</span></p>
              <table className="w-full table-fixed text-sm">
                <tbody>
                  {parameters.reduce((rows, param, index) => {
                    if (index % 2 === 0) rows.push([param]);
                    else rows[rows.length - 1].push(param);
                    return rows;
                  }, []).map((row, rowIndex) => (
                    <tr key={rowIndex} className="align-bottom">
                      {row.map(param => (
                        <td key={param.name} className="p-2 w-1/2">
                          <div className="flex flex-col">
                            <label className="block text-[#ccc] mb-1">{param.prompt}</label>
                            <input
                              type={param.type === 'boolean' ? 'checkbox' : 'text'}
                              value={param.type === 'boolean' ? undefined : values[param.name] ?? ""}
                              checked={param.type === 'boolean' ? values[param.name] || false : undefined}
                              onChange={(e) => updateValue(param.name, param.type === 'boolean' ? e.target.checked : parseInt(e.target.value, 10))}
                              className="bg-[#2a2a2a] text-white border border-gray-600 rounded px-3 py-2"
                              style={{ borderRadius: "30px" }}
                            />
                          </div>
                        </td>
                      ))}
                      {row.length < 2 && <td className="w-1/2"></td>}
                    </tr>
                  ))}
                </tbody>
              </table>
            </>
          )}
        </div>
      </div>

      {/* FEE RESULT */}
      {feeResult && (
        <div className="fixed bottom-0 left-0 right-0 bg-gradient-to-r from-indigo-700 to-indigo-900 text-white p-4 shadow-inner mt-8">
          <div className="flex justify-between items-center">
            <div className="text-lg font-semibold">API call estimate:</div>
            <div className="text-xl font-bold">{feeResult.fee.toFixed(5)} USD</div>
          </div>
          {feeResult.details && (
            <div className="mt-3">
              <div className="text-sm font-semibold text-gray-300 mb-2 text-center">Breakdown of fees</div>
              <div className="bg-[#2c2c2c] rounded-md p-3 mx-auto text-sm text-gray-300 max-w-6xl overflow-x-auto">
                <table className="w-full text-xs">
                  <tbody>
                    {Object.entries(feeResult.details).reduce((rows, [label, detail], index) => {
                      const rowIndex = Math.floor(index / 3);
                      if (!rows[rowIndex]) rows[rowIndex] = [];
                      rows[rowIndex].push(
                        <td key={label} className="px-3 py-1 text-center w-1/3">
                          <div className="border border-gray-600 rounded p-2">
                            {label} (x{detail.value}): {detail.fee.toFixed(5)} USD
                          </div>
                        </td>
                      );
                      return rows;
                    }, []).map((row, i) => (
                      <tr key={i} className="w-full">
                        {row}
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            </div>
          )}
        </div>
      )}
    </div>
  );
}