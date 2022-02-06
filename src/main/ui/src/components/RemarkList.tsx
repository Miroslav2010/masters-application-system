import {List, ListItem, ListItemText, Stack, Typography} from "@mui/material";
import React from "react";
import IconButton from "@mui/material/IconButton";
import EditIcon from "@mui/icons-material/Edit";
import DeleteIcon from "@mui/icons-material/Delete";
import {RemarkDTO} from "../domain/remarkResponse";

interface Props {
    remarks: RemarkDTO[],
    editRemark?: Function,
    deleteRemark?: Function
}

const RemarkList = (props: Props) => {
    return (
    <List sx={{ bgcolor: 'background.paper', display: 'flex', flexDirection: 'column', justifyContent: 'center', alignItems: 'center', marginTop: '30px' }}>
        {props.remarks.map((value, index) => (
            <ListItem key={index}  sx={{ border: '1px solid grey', borderRadius: '5px', width: '90%', display: 'block', marginBottom: '20px'}}
                      secondaryAction={
                          value.personName === 'Mentor' && props.editRemark != undefined && props.deleteRemark != undefined &&
                          <Stack>
                              <IconButton onClick={() => props.editRemark != undefined ? props.editRemark(index) : ""}>
                                  <EditIcon />
                              </IconButton>
                              <IconButton onClick={() => props.deleteRemark != undefined ? props.deleteRemark(index) : ""}>
                                  <DeleteIcon />
                              </IconButton>
                          </Stack>
                      }>
                <ListItemText
                    primary= {
                        <Typography
                            component="div"
                            variant="h6"
                            color="text.secondary">
                            <strong>{value.personName}</strong>
                        </Typography>}
                    secondary={
                        <Typography
                            sx={{  fontSize: 20 }}
                            component="div"
                            variant="body2"
                            color="text.secondary">
                            {value.remark}
                        </Typography>
                    }/>
            </ListItem>
        ))}
    </List>
    )
}

export default RemarkList;
