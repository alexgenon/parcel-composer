import {
    Button,
    Checkbox,
    FormControl,
    FormControlLabel,
    FormLabel,
    Radio,
    RadioGroup,
    Stack,
    Typography
} from "@mui/material";
import React, {createRef, MutableRefObject, RefObject, useRef, useState} from "react";
import {useLoadAddressesMutation} from "./AddressLoaderApi";

function getSelectedFile(ref: RefObject<HTMLInputElement>): File | undefined {
    console.log(ref);
    return ref.current?.files?.[0];
}

function AddressLoader() {
    const [loadingMode, setLoadingMode] = useState("MERGE");
    const changeLoadingMode = (event: React.ChangeEvent<HTMLInputElement>) => {
        setLoadingMode(event.target.value);
    }

    const [legacyFormat, setLegacyFormat] = useState(true);
    const changeLegacyFormat = (event: React.ChangeEvent<HTMLInputElement>) => {
        setLegacyFormat(event.target.checked);
    }

    let fileRef = createRef<HTMLInputElement>();
    const [loadAddresses] = useLoadAddressesMutation();
    const loadFiles = (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        let loadingFile = getSelectedFile(fileRef);
        if (loadingFile) {
            console.log(`About to load ${loadingFile.name} using ${loadingMode} strategy`)
            let form = new FormData();
            form.append("file", loadingFile);
            form.append("loading_strategy", loadingMode);
            form.append("legacy_format", legacyFormat ? "true" : "false");
            loadAddresses(form);
        } else {
            console.error("No file submitted");
            window.alert("Veuillez sélectionner un fichier");
        }
    }

    return (
        <>
            <Typography variant="h2">Charger fichier</Typography>
            <form onSubmit={loadFiles}>
                <Stack>
                    <FormControl>
                        <FormLabel id="load_mode">Méthode de chargement</FormLabel>
                        <RadioGroup
                            row
                            value={loadingMode}
                            onChange={changeLoadingMode}
                        >
                            <FormControlLabel control={<Radio/>} label="Ajouter au carnet existant"
                                              value="MERGE"/>
                            <FormControlLabel control={<Radio/>} label="Effacer carnet existant"
                                              value="TRUNCATE_AND_LOAD"/>
                        </RadioGroup>
                    </FormControl>
                    <FormControlLabel control={<Checkbox onChange={changeLegacyFormat} defaultChecked/>}
                                      label="Ancien format de fichier"/>
                    <input
                        type="file"
                        id="file_selector"
                        ref={fileRef}
                    />
                    <Button variant="outlined" type="submit">Charger</Button>
                </Stack>
            </form>
        </>
    );
}

export default AddressLoader;
