import React from 'react';
import './App.css';
import {Provider} from "react-redux";
import {store} from "./app/store";
import ParcelComposer from "./Parcel/Parcel-composer";

function App() {
  return (
    <Provider store={store}>
    <ParcelComposer/>
    </Provider>
  );
}

export default App;
