import * as React from 'react';
import Box from '@mui/material/Box';
import {Button, Card, CardActions, CardContent, Chip, Grid, Paper, Stack, TextField, Typography} from "@mui/material";
import styles from "../ValidationPage.style.module.css";
import {styled} from "@mui/styles";
import Divider from "@mui/material/Divider";
import masterService from "../../service/masterService";


interface Props {
    processId: string
}

interface IUseState {
    topic: string,
    description: string,
    biography: File | null,
    application: File | null,
    mentorApproval: File | null,
    supplement: File | null
}

export default function DocumentUpload(props: Props) {
    const Input = styled('input')({
        display: 'none',
    });
    const [formData, updateFormData] = React.useState<IUseState>({
        topic: "",
        description: "",
        biography: null,
        application: null,
        mentorApproval: null,
        supplement: null
    })

    let handleChange = (e: any) => {
        console.log(e.target.value);
        console.log(e.target.value.trim())
        updateFormData({
            ...formData,
            [e.target.name]: e.target.value.trim()
        })
    }

    const onfileAttached = (event: any) => {
        console.log(event.target.files[0]);
        console.log(event.target.name);
        // setFile(event.target.files[0])
        updateFormData({
            ...formData,
            [event.target.name]: event.target.files[0]
        });
    }

    const updateIsDisabled = () => {
        return formData.topic == "" || formData.description == "" || formData.application == null ||
            formData.mentorApproval == null || formData.biography == null || formData.supplement == null;
    }

    const onSubmit = () => {
        console.log(formData.topic)
        console.log(formData.description)
        console.log(formData.application)
        console.log(formData.mentorApproval)
        console.log(formData.biography)
        console.log(formData.supplement)

        masterService.createMasterTopic(
            props.processId,
            formData.topic,
            formData.description,
            formData.application,
            formData.mentorApproval,
            formData.biography,
            formData.supplement
        ).then(r => {
            console.log(r);
            window.location.reload();
        });
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
                        Прикачување на документи
                    </Typography>
                    <hr className={styles.hr}/>
                    <TextField fullWidth label="Тема" id="topic" name="topic" sx={{maxWidth: '70%', marginTop: '40px'}}
                    onChange={handleChange} />

                    <TextField fullWidth multiline rows={5} label="Опис" id="description" name="description"
                               sx={{maxWidth: '70%', marginTop: '40px'}} onChange={handleChange} />
                    <Box sx={{ width: '100%', marginTop: '80px' }}>
                        <Grid container columnSpacing={{ xs: 1, sm: 2, md: 3 }}>
                            <Grid item xs={3}>
                                <Divider>
                                    <Chip label="Апликација" />
                                </Divider>
                            </Grid>
                            <Grid item xs={3}>
                                <Divider>
                                    <Chip label="Одобрение од ментор" />
                                </Divider>
                            </Grid>
                            <Grid item xs={3}>
                                <Divider>
                                    <Chip label="Биографија" />
                                </Divider>
                            </Grid>
                            <Grid item xs={3}>
                                <Divider>
                                    <Chip label="Дополнителни документи" />
                                </Divider>
                            </Grid>

                            <Grid item xs={3} sx={{ marginTop: '15px', textAlign: 'center'}}>
                                <label htmlFor="application">
                                    <Typography gutterBottom variant="body1" component="span" color="text.secondary">
                                        {formData.application != null ? formData.application.name : ""}
                                    </Typography>
                                    <Input id="application" name="application" type="file"
                                           onChange={onfileAttached}  />
                                    <Button variant="outlined" component="div" sx={{ display: 'block'}}>
                                        Прикачи
                                    </Button>
                                </label>
                            </Grid>
                            <Grid item xs={3} sx={{ marginTop: '15px', textAlign: 'center'}}>
                                <label htmlFor="mentorApproval">
                                    <Typography gutterBottom variant="body1" component="span" color="text.secondary">
                                        {formData.mentorApproval != null ? formData.mentorApproval.name : ""}
                                    </Typography>
                                    <Input id="mentorApproval" name="mentorApproval" type="file"
                                           onChange={onfileAttached}  />
                                    <Button variant="outlined" component="div" sx={{ display: 'block'}}>
                                        Прикачи
                                    </Button>
                                </label>
                            </Grid>
                            <Grid item xs={3} sx={{ marginTop: '15px', textAlign: 'center'}}>
                                <label htmlFor="biography">
                                    <Typography gutterBottom variant="body1" component="span" color="text.secondary">
                                        {formData.biography != null ? formData.biography.name : ""}
                                    </Typography>
                                    <Input id="biography" name="biography" type="file"
                                           onChange={onfileAttached}  />
                                    <Button variant="outlined" component="div" sx={{ display: 'block'}}>
                                        Прикачи
                                    </Button>
                                </label>
                            </Grid>
                            <Grid item xs={3} sx={{ marginTop: '15px', textAlign: 'center'}}>
                                <label htmlFor="supplement">
                                    <Typography gutterBottom variant="body1" component="span" color="text.secondary">
                                        {formData.supplement != null ? formData.supplement.name : ""}
                                    </Typography>
                                    <Input id="supplement" name="supplement" type="file"
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