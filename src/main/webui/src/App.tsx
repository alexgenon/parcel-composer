import React from 'react';
import './App.css';
import {Provider} from "react-redux";
import {store} from "./app/store";
import ParcelComposer from "./Parcel/Parcel-composer";
import { BrowserRouter, Routes, Route, Outlet } from "react-router-dom";
import MenuBar from "./MenuBar";
import AddressBook from "./Address/AddressBook";
import CustomerOnMap from "./CustomerOnMap/CustomerOnMap";

export type ApplicationScreens = { name: string, path: string, element: React.ReactNode, displayOnMenu: boolean, default: boolean }

const screens: ApplicationScreens[] = [{
    name: "Préparer envois",
    path: "/",
    element: <ParcelComposer/>,
    default: true,
    displayOnMenu: true
}, {
    name: "Carnet adresses",
    path: "/addresses",
    element: <AddressBook/>,
    default: false,
    displayOnMenu: true
}, {
    name: "Carte clients",
    path: "/map",
    element: <CustomerOnMap/>,
    default: false,
    displayOnMenu: true
}]

function App() {
    return (
        <Provider store={store}>
            <BrowserRouter>
                <Routes>
                    <Route path="/" element={<AppLayout screens={screens}/>}>
                        {screens.map(screen => {
                            if (screen.default) {
                                return (<Route key = {screen.path} index element={screen.element}/>);
                            } else {
                                return(<Route key = {screen.path} path={screen.path} element={screen.element}/>);
                            }
                        })}
                    </Route>
                </Routes>
            </BrowserRouter>
        </Provider>
    );
}

interface AppLayoutProps {
    screens: ApplicationScreens[]
}

function AppLayout({screens}: AppLayoutProps) {
    return (<>
            <MenuBar screens={screens}/>
            <Outlet/>
        </>
    );
}
export default App;
