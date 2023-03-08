import React from 'react';
import './App.css';
import {Provider} from "react-redux";
import {store} from "./app/store";
import ParcelComposer from "./Parcel/Parcel-composer";
import { BrowserRouter, Routes, Route, Outlet } from "react-router-dom";
import MenuBar from "./MenuBar";
import AddressBook from "./AddressBook/AddressBook";
import CustomerOnMap from "./CustomerOnMap/CustomerOnMap";
import {AuthContextProps, useAuth} from "react-oidc-context";

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
    const auth = useAuth();
    console.log("auth: ");
    console.log(auth);
    switch (auth.activeNavigator) {
        case "signinSilent":
            return <div>Signing you in...</div>;
        case "signoutRedirect":
            return <div>Signing you out...</div>;
    }

    if (auth.isLoading) {
        return <div>Loading...</div>;
    }

    if (auth.error) {
        return <div>Oops... {auth.error.message}</div>;
    }


        return (
            <Provider store={store}>
                <BrowserRouter>
                    <Routes>
                        <Route path="/" element={<AppLayout screens={screens} auth={auth}/>}>
                            {screens.map(screen => {
                                if (screen.default) {
                                    return (<Route key={screen.path} index element={screen.element}/>);
                                } else {
                                    return (<Route key={screen.path} path={screen.path} element={screen.element}/>);
                                }
                            })}
                        </Route>
                    </Routes>
                </BrowserRouter>
            </Provider>
        );
}

interface AppLayoutProps {
    screens: ApplicationScreens[],
    auth: AuthContextProps
}

function AppLayout({screens, auth}: AppLayoutProps) {
    return (<>
            <MenuBar screens={screens} loggedInUser={auth.user?.profile.given_name} loginCallback={auth.signinRedirect} logoutCallback={auth.signoutRedirect}/>
            <Outlet/>
        </>
    );
}

export default App;
