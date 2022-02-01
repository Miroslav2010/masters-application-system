import React from "react";
import {PageLayout} from "../../PageLayout";
import {
    Button,
    Divider, FormControl,
    Grid, InputLabel, ListItemText, MenuItem, OutlinedInput, Select, SelectChangeEvent,
    TextField, Typography
} from "@mui/material";
import personService from "../../service/personService";
import {PersonDto} from "../../domain/PersonDto";
import {MajorDto} from "../../domain/MajorDto";
import masterService from "../../service/masterService";

interface IProps {
}

interface IState {
    professors: PersonDto[];
    majors: MajorDto[];
    majorId: string;
    mentorId: string;
    firstCommittee: string;
    secondCommittee: string;
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
        this.state = {
            professors: [],
            majors: [],
            majorId: "",
            mentorId: "",
            firstCommittee: "",
            secondCommittee: ""
        }
    }

    componentDidMount() {
        personService.fetchProfessors((data) => {
            this.setState({professors: data})
        })
        masterService.getAllMajors((data) => {
            this.setState({majors: data})
        })
    }

    componentDidUpdate(prevProps: Readonly<IProps>, prevState: Readonly<IState>, snapshot?: any) {

    }

    setMajor(event: SelectChangeEvent<string>) {
        const {
            target: {value},
        } = event;
        this.setState({
            majorId: value
        })
    }

    setMentor(event: SelectChangeEvent<string>) {
        const {
            target: {value},
        } = event;
        this.setState({
            mentorId: value
        })
    }

    setCommitteeFirst(event: SelectChangeEvent<string>){
        const {
            target: {value},
        } = event;
        this.setState({
            firstCommittee: value
        })
    }

    setCommitteeSecond(event: SelectChangeEvent<string>){
        const {
            target: {value},
        } = event;
        this.setState({
            secondCommittee: value
        })
    }

    handleSubmit(){
        masterService.createMaster({majorId:this.state.majorId,mentorId:this.state.mentorId,firstCommitteeId: this.state.firstCommittee,secondCommitteeId:this.state.secondCommittee})
    }

    render() {
        return (
            <PageLayout>
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
                        <Typography gutterBottom variant="h5" component="div" color="text.secondary">
                            Пријава
                        </Typography>
                    </Grid>
                    <Grid
                        item
                        container
                        direction="row"
                        justifyContent="center"
                        alignItems="center"
                        xs={6}>
                        <FormControl sx={{m: 1, width: 200}}>
                            <InputLabel id="majorLabel">Насока</InputLabel>
                            <Select id="major" labelId="majorLabel" onChange={(value) => {
                                this.setMajor(value)
                            }} value={this.state.majorId} label="Насока" input={<OutlinedInput label="Насока"/>}
                                    MenuProps={MenuProps}>
                                {
                                    this.state.majors.map((major) => {
                                        return (
                                            <MenuItem key={major.id} value={major.id}>
                                                <ListItemText primary={major.name}/>
                                            </MenuItem>
                                        )
                                    })
                                }
                            </Select>
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
                            <InputLabel id="mentorLabel">Ментор</InputLabel>
                            <Select id="mentor" labelId="mentorLabel" onChange={(value) => {
                                this.setMentor(value)
                            }} value={this.state.mentorId} label="Ментор" input={<OutlinedInput label="Ментор"/>}
                                    MenuProps={MenuProps}>
                                {
                                    this.state.professors.map((prof) => {
                                        return (
                                            <MenuItem key={prof.id} value={prof.id}>
                                                <ListItemText primary={prof.fullName}/>
                                            </MenuItem>
                                        )
                                    })
                                }
                            </Select>
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
                            <InputLabel id="committee1Label">Комисија 1</InputLabel>
                            <Select id="committee1" labelId="committeeLabel" onChange={(value) => {
                                this.setCommitteeFirst(value)
                            }} value={this.state.firstCommittee} label="Комисија 1" input={<OutlinedInput label="Комисија 1"/>}
                                    MenuProps={MenuProps}>
                                {
                                    this.state.professors.map((prof) => {
                                        return (
                                            <MenuItem key={prof.id} value={prof.id}>
                                                <ListItemText primary={prof.fullName}/>
                                            </MenuItem>
                                        )
                                    })
                                }
                            </Select>
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
                            <InputLabel id="committee2Label">Комисија 2</InputLabel>
                            <Select id="committee2" labelId="committee2Label" onChange={(value) => {
                                this.setCommitteeSecond(value)
                            }} value={this.state.secondCommittee} label="Комисија 2" input={<OutlinedInput label="Комисија 2"/>}
                                    MenuProps={MenuProps}>
                                {
                                    this.state.professors.map((prof) => {
                                        return (
                                            <MenuItem key={prof.id} value={prof.id}>
                                                <ListItemText primary={prof.fullName}/>
                                            </MenuItem>
                                        )
                                    })
                                }
                            </Select>
                        </FormControl>
                    </Grid>
                    <Grid
                    item
                    container
                    xs={12}
                    justifyContent="flex-end">
                        <Button variant="contained" color="success" onClick={()=>{this.handleSubmit()}}>Потврди</Button>
                    </Grid>
                </Grid>
            </PageLayout>
        )
    }
}

export default MasterTopic