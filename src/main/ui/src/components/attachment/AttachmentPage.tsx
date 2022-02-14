import * as React from 'react';
import Box from '@mui/material/Box';
import {Button, Card, CardActions, CardContent, Chip, Grid, Typography} from "@mui/material";
import styles from "../ValidationPage.style.module.css";
import {styled} from "@mui/styles";
import Divider from "@mui/material/Divider";
import masterService from "../../service/masterService";
import {useState} from "react";


interface Props {
    processId: string,
    label: string | undefined
}

interface IUseState {
    draft: File | null
}

export default function AttachmentPage(props: Props) {
    const [isDisabled, setIsDisabled] = useState(false);
    const Input = styled('input')({
        display: 'none',
    });
    const [formData, updateFormData] = React.useState<IUseState>({
        draft: null
    })

    const onfileAttached = (event: any) => {
        updateFormData({
            ...formData,
            [event.target.name]: event.target.files[0]
        });
    }

    const updateIsDisabled = () => {
        return formData.draft == null ? !isDisabled : isDisabled;
    }

    const onSubmit = () => {
        console.log(formData.draft)

        setIsDisabled(true);
        masterService.uploadDraft(
            props.processId,
            formData.draft
        ).then(r => {
            console.log(r);
            window.location.reload();
        });
    }

    const translate = (name: string | undefined) => {
        return masterService.translate(name!);
    }

    return (
        <Box component="div" sx={{
            display: 'block',
            transform: 'scale(0.9)',
            marginTop: '10px',
            border: '2px solid grey',
            borderRadius: '5px'
        }}>
            <Card variant="outlined" sx={{minHeight: '85vh', minWidth: '75vw'}}
                  className={`${styles.flex} ${styles.flexColumnDirection} ${styles.spaceBetween}`}>
                <CardContent>
                    <Typography gutterBottom variant="h5" component="div" color="text.secondary">
                        {translate(props.label)}
                    </Typography>
                    <hr className={styles.hr}/>
                    <Typography gutterBottom variant="h5" component="div" color="text.secondary" sx={{marginTop: '35px'}}>
                        {props.label == "MENTOR_REPORT" ? "Место за прикачување на извештајот на менторот" : "Место за прикачување на магистерски труд"}
                    </Typography>
                    <Box sx={{ width: '100%', marginTop: '80px' }}>
                        <Grid container columnSpacing={{ xs: 1, sm: 2, md: 3 }}>
                            <Grid item xs={12}>
                                <Divider>
                                    <Chip label="Магистерски Труд" />
                                </Divider>
                            </Grid>

                            <Grid item xs={3} sx={{ margin: 'auto', marginTop: '15px', textAlign: 'center'}}>
                                <label htmlFor="draft">
                                    <Typography gutterBottom variant="body1" component="span" color="text.secondary">
                                        {formData.draft != null ? formData.draft.name : ""}
                                    </Typography>
                                    <Input id="draft" name="draft" type="file"
                                           onChange={onfileAttached}  />
                                    <Button variant="outlined" component="div" sx={{ display: 'block'}}>
                                        Прикачи
                                    </Button>
                                </label>
                            </Grid>
                        </Grid>
                    </Box>

                </CardContent>
                <CardActions sx={{marginBottom: "20px", marginRight: "45px", justifyContent: 'end'}}>
                    <Button variant="contained" color="success" onClick={() => onSubmit()}  disabled={updateIsDisabled()}>Потврди</Button>
                </CardActions>
            </Card>
        </Box>
    );
}