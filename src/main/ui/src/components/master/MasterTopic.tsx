import React from "react";
import {
    Box,
    Button,
    Card,
    CardActions,
    CardContent,
    FormControl,
    Grid,
    InputLabel,
    ListItemText,
    MenuItem,
    OutlinedInput,
    Select,
    SelectChangeEvent,
    Typography
} from "@mui/material";
import personService from "../../service/personService";
import {PersonDto} from "../../domain/PersonDto";
import {MajorDto} from "../../domain/MajorDto";
import masterService from "../../service/masterService";
import styles from "../ValidationPage.style.module.css";
import {PageLayout} from "../../PageLayout";
import {NavigateFunction, useNavigate} from "react-router-dom";
import {StudentDto} from "../../domain/StudentDto";
import Autocomplete from '@mui/material/Autocomplete';
import TextField from '@mui/material/TextField';

interface IProps {
    navigation: NavigateFunction
}

interface IState {
    professors: PersonDto[];
    students: StudentDto[];
    majors: MajorDto[];
    majorId: string;
    studentId: string;
    firstCommittee: string;
    secondCommittee: string;
    isDisabled: boolean;
}

const MenuProps = {
    PaperProps: {
        style: {
            width: 250,
        },
    },
};

class MasterTopic extends React.Component<IProps, IState> {

    constructor(props: IProps) {
        super(props);
        console.log(props);
        this.state = {
            professors: [],
            students: [],
            majors: [],
            majorId: "",
            studentId: "",
            firstCommittee: "",
            secondCommittee: "",
            isDisabled: false
        }
    }

    componentDidMount() {
        personService.fetchProfessors((data) => {
            this.setState({professors: data})
        })
        personService.fetchStudents((data) => {
            this.setState({students: data})
        })
        masterService.getAllMajors((data) => {
            this.setState({majors: data})
        })
    }

    componentDidUpdate(prevProps: Readonly<IProps>, prevState: Readonly<IState>, snapshot?: any) {

    }

    setMajor(value: string | undefined) {
        let selectedMajor = value == undefined ? "" : value;
        console.log(selectedMajor);
        this.setState({
            majorId: selectedMajor
        });
    }

    setStudent(value: string | undefined) {
        let selectedStudent = value == undefined ? "" : value;
        console.log(selectedStudent);
        this.setState({
            studentId: selectedStudent
        });
    }

    setCommitteeFirst(value: string | undefined) {
        let selectedProfessor = value == undefined ? "" : value;
        console.log(selectedProfessor);
        this.setState({
            firstCommittee: selectedProfessor
        });
    }

    setCommitteeSecond(value: string | undefined) {
        let selectedProfessor = value == undefined ? "" : value;
        console.log(selectedProfessor);
        this.setState({
            secondCommittee: selectedProfessor
        });
    }

    handleSubmit() {
        this.setState({
            isDisabled: true
        })
        masterService.createMaster({
            majorId: this.state.majorId,
            studentId: this.state.studentId,
            firstCommitteeId: this.state.firstCommittee,
            secondCommitteeId: this.state.secondCommittee
        }).then(res => this.props.navigation("/masters"));
    }

    updateIsDisabled() {
        return (this.state.majorId == "" || this.state.studentId == "" || this.state.firstCommittee == "" ||
            this.state.secondCommittee == "") ? !this.state.isDisabled : this.state.isDisabled;
    }

    render() {

        return (
            <PageLayout>
                <Box component="div" sx={{
                    display: 'block',
                    transform: 'scale(0.9)',
                    marginTop: '10px',
                    border: '2px solid grey',
                    borderRadius: '5px'
                }}>
                    <Card variant="outlined" sx={{minHeight: '65vh'}}
                          className={`${styles.flex} ${styles.flexColumnDirection} ${styles.spaceBetween}`}>
                        <CardContent>
                            <Grid
                                container
                                direction="row"
                                justifyContent="center"
                                alignItems="center"
                                marginTop={1}
                                rowSpacing={1}>
                                <Grid
                                    item
                                    justifyContent="flex-start"
                                    alignItems="center"
                                    xs={12}>
                                    <Typography gutterBottom variant="h5" component="div" color="text.secondary" sx={{fontSize: '35px'}}>
                                        Пријава
                                    </Typography>
                                </Grid>
                                <Grid
                                    item
                                    justifyContent="flex-start"
                                    alignItems="center"
                                    xs={12}>
                                    <hr className={styles.hr}/>
                                </Grid>
                                <Grid
                                    item
                                    justifyContent="flex-start"
                                    alignItems="center"
                                    xs={12}>
                                    <Typography gutterBottom variant="h5" component="div" color="text.secondary">
                                        Потребно е да се избере студент за кого се однесува магистерската,
                                        насока и членови на комисија
                                    </Typography>
                                </Grid>
                                <Grid
                                    item
                                    container
                                    direction="row"
                                    justifyContent="center"
                                    alignItems="center"
                                    sx={{ marginTop: '30px'}}
                                    xs={6}>
                                    <FormControl sx={{m: 1, width: 200}}>
                                        <Autocomplete
                                            disablePortal
                                            id="major"
                                            options={this.state.majors}
                                            getOptionLabel={option => option.name}
                                            sx={{ width: 250 }}
                                            renderInput={(params) => <TextField {...params} label="Насока" />}
                                            onChange={(event, newValue) => {
                                                console.log(newValue?.id);
                                                this.setMajor(newValue?.id)
                                            }}
                                        />
                                    {/*    <InputLabel id="majorLabel">Насока</InputLabel>*/}
                                    {/*    <Select id="major" labelId="majorLabel" onChange={(value) => {*/}
                                    {/*        this.setMajor(value)*/}
                                    {/*    }} value={this.state.majorId} label="Насока"*/}
                                    {/*            input={<OutlinedInput label="Насока"/>}*/}
                                    {/*            MenuProps={MenuProps}>*/}
                                    {/*        {*/}
                                    {/*            this.state.majors.map((major) => {*/}
                                    {/*                return (*/}
                                    {/*                    <MenuItem key={major.id} value={major.id}>*/}
                                    {/*                        <ListItemText primary={major.name}/>*/}
                                    {/*                    </MenuItem>*/}
                                    {/*                )*/}
                                    {/*            })*/}
                                    {/*        }*/}
                                    {/*    </Select>*/}
                                    </FormControl>
                                </Grid>
                                <Grid
                                    item
                                    container
                                    direction="row"
                                    justifyContent="center"
                                    sx={{ marginTop: '30px'}}
                                    alignItems="center"
                                    xs={6}>
                                    <FormControl sx={{m: 1, width: 200}}>
                                        <Autocomplete
                                            disablePortal
                                            id="student"
                                            options={this.state.students}
                                            getOptionLabel={option => option.fullName + ", " + option.index}
                                            sx={{ width: 250 }}
                                            renderInput={(params) => <TextField {...params} label="Студент" />}
                                            onChange={(event, newValue) => {
                                                console.log(newValue?.id);
                                                this.setStudent(newValue?.id)
                                            }}
                                        />
                                        {/*<InputLabel id="studentLabel">Студент</InputLabel>*/}
                                        {/*<Select id="student" labelId="studentLabel" onChange={(value) => {*/}
                                        {/*    this.setStudent(value)*/}
                                        {/*}} value={this.state.studentId} label="Студент"*/}
                                        {/*        input={<OutlinedInput label="Студент"/>}*/}
                                        {/*        MenuProps={MenuProps}>*/}
                                        {/*    {*/}
                                        {/*        this.state.students.map((student) => {*/}
                                        {/*            return (*/}
                                        {/*                <MenuItem key={student.id} value={student.id}>*/}
                                        {/*                    <ListItemText primary={student.fullName + ", " + student.index }/>*/}
                                        {/*                </MenuItem>*/}
                                        {/*            )*/}
                                        {/*        })*/}
                                        {/*    }*/}
                                        {/*</Select>*/}
                                    </FormControl>
                                </Grid>
                                <Grid
                                    item
                                    container
                                    direction="row"
                                    justifyContent="center"
                                    alignItems="center"
                                    xs={6}>
                                    <FormControl sx={{m: 1, width: 200}}>
                                        <Autocomplete
                                            disablePortal
                                            id="firstCommittee"
                                            options={this.state.professors}
                                            getOptionLabel={option => option.fullName}
                                            sx={{ width: 250 }}
                                            renderInput={(params) => <TextField {...params} label="Комисија 1" />}
                                            onChange={(event, newValue) => {
                                                console.log(newValue?.id);
                                                this.setCommitteeFirst(newValue?.id)
                                            }}
                                        />
                                        {/*<InputLabel id="committee1Label">Комисија 1</InputLabel>*/}
                                        {/*<Select id="committee1" labelId="committeeLabel" onChange={(value) => {*/}
                                        {/*    this.setCommitteeFirst(value)*/}
                                        {/*}} value={this.state.firstCommittee} label="Комисија 1"*/}
                                        {/*        input={<OutlinedInput label="Комисија 1"/>}*/}
                                        {/*        MenuProps={MenuProps}>*/}
                                        {/*    {*/}
                                        {/*        this.state.professors.map((prof) => {*/}
                                        {/*            return (*/}
                                        {/*                <MenuItem key={prof.id} value={prof.id}>*/}
                                        {/*                    <ListItemText primary={prof.fullName}/>*/}
                                        {/*                </MenuItem>*/}
                                        {/*            )*/}
                                        {/*        })*/}
                                        {/*    }*/}
                                        {/*</Select>*/}
                                    </FormControl>
                                </Grid>
                                <Grid
                                    item
                                    container
                                    direction="row"
                                    justifyContent="center"
                                    alignItems="center"
                                    xs={6}>
                                    <FormControl sx={{m: 1, width: 200}}>
                                        <Autocomplete
                                            disablePortal
                                            id="secondCommittee"
                                            options={this.state.professors}
                                            getOptionLabel={option => option.fullName}
                                            sx={{ width: 250 }}
                                            renderInput={(params) => <TextField {...params} label="Комисија 2" />}
                                            onChange={(event, newValue) => {
                                                console.log(newValue?.id);
                                                this.setCommitteeSecond(newValue?.id)
                                            }}
                                        />
                                        {/*<InputLabel id="committee2Label">Комисија 2</InputLabel>*/}
                                        {/*<Select id="committee2" labelId="committee2Label" onChange={(value) => {*/}
                                        {/*    this.setCommitteeSecond(value)*/}
                                        {/*}} value={this.state.secondCommittee} label="Комисија 2"*/}
                                        {/*        input={<OutlinedInput label="Комисија 2"/>}*/}
                                        {/*        MenuProps={MenuProps}>*/}
                                        {/*    {*/}
                                        {/*        this.state.professors.map((prof) => {*/}
                                        {/*            return (*/}
                                        {/*                <MenuItem key={prof.id} value={prof.id}>*/}
                                        {/*                    <ListItemText primary={prof.fullName}/>*/}
                                        {/*                </MenuItem>*/}
                                        {/*            )*/}
                                        {/*        })*/}
                                        {/*    }*/}
                                        {/*</Select>*/}
                                    </FormControl>
                                </Grid>
                            </Grid>
                        </CardContent>
                        <CardActions sx={{marginBottom: "20px", marginRight: "45px", justifyContent: 'end'}}>
                            <Grid
                                item
                                container
                                xs={12}
                                justifyContent="flex-end">
                                <Button variant="contained" color="success" disabled={this.updateIsDisabled()} onClick={() => {
                                    this.handleSubmit()
                                }}>Потврди</Button>
                            </Grid>
                        </CardActions>
                    </Card>
                </Box>
            </PageLayout>
        )
    }
}

export default function(props: any) {
    // const navigation = useNavigate();

    return <MasterTopic
        {...props} navigation={useNavigate()}/>
}