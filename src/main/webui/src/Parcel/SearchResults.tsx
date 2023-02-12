import {Address} from "../Address/Address";
import {IconButton, List, ListItem, ListItemButton, ListItemIcon, ListItemText} from "@mui/material";
import {Add, Delete} from "@mui/icons-material";
import React from "react";

export interface SearchResultsProps {
    candidateAddresses: Address[]
    addressClicked: (address: Address) => void
    removeAddress: (address: Address) => void
}

function SearchResults({candidateAddresses, addressClicked,removeAddress}: SearchResultsProps) {
    if (candidateAddresses.length > 0) {
        return (
            <List>
                {candidateAddresses.map(address =>
                    <ListItem key={address.businessId}
                              secondaryAction={
                                  <IconButton arial-label="supprimer l'address"
                                              onClick={_ => removeAddress(address)}
                                  >
                                      <Delete/>
                                  </IconButton>
                              }
                    >
                        <ListItemButton onClick={_ => addressClicked(address)} aria-label="ajouter l'addresse">
                            <ListItemIcon>
                                <Add/>
                            </ListItemIcon>
                            <ListItemText primary={address.firstName + " " + address.lastName}
                                          secondary={address.city}
                            />
                        </ListItemButton>
                    </ListItem>
                )}
            </List>
        )
    } else {
        return (<div><em>Pas d'addresse trouvée</em></div>);
    }
}


export default SearchResults
