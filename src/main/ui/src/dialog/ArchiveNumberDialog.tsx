import DialogTitle from "@mui/material/DialogTitle";
import DialogContent from "@mui/material/DialogContent";
import TextField from "@mui/material/TextField";
import DialogActions from "@mui/material/DialogActions";
import {Button} from "@mui/material";
import Dialog from "@mui/material/Dialog";
import React from "react";

interface Props {
    open: boolean,
    onClose: Function
}

const ArchiveNumberDialog = (props: Props) => {
    const [archiveNumber, setArchiveNumber] = React.useState(undefined);

    const handleChanges = (event: any) => {
        setArchiveNumber(event.target.value);
        console.log(archiveNumber);
    };

    console.log(props);
    return (
        <Dialog fullWidth={true} open={props.open} onClose={() => {
            props.onClose(undefined);
        }}>
            <DialogTitle>Архивски број</DialogTitle>
            <DialogContent>
                <TextField
                    autoFocus
                    margin="dense"
                    id="name"
                    label="Архивски број"
                    multiline={false}
                    defaultValue={''}
                    onChange={(e) => handleChanges(e)}
                    fullWidth
                    variant="standard"
                />
            </DialogContent>
            <DialogActions>
                <Button onClick={() => props.onClose(undefined)}>Cancel</Button>
                <Button onClick={() => props.onClose(archiveNumber)}>Save</Button>
            </DialogActions>
        </Dialog>
    )
}

export default ArchiveNumberDialog;
