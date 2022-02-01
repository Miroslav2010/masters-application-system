import DialogTitle from "@mui/material/DialogTitle";
import DialogContent from "@mui/material/DialogContent";
import TextField from "@mui/material/TextField";
import DialogActions from "@mui/material/DialogActions";
import {Button} from "@mui/material";
import Dialog from "@mui/material/Dialog";
import React from "react";

interface Props {
    open: boolean,
    onClose: Function,
    defaultRemark: string,
    handleChanges: Function
}

const RemarkDialog = (props: Props) => {
    console.log(props);
    return (
        <Dialog fullWidth={true} open={props.open} onClose={() => {
            props.onClose(false);
        }}>
            <DialogTitle>Remark</DialogTitle>
            <DialogContent>
                <TextField
                    autoFocus
                    margin="dense"
                    id="name"
                    label="Remark"
                    multiline={true}
                    defaultValue={props.defaultRemark}
                    onChange={(e) => props.handleChanges(e)}
                    rows={4}
                    fullWidth
                    variant="standard"
                />
            </DialogContent>
            <DialogActions>
                <Button onClick={() => props.onClose(false)}>Cancel</Button>
                <Button onClick={() => props.onClose(true)}>Save</Button>
            </DialogActions>
        </Dialog>
    )
}

export default RemarkDialog;
