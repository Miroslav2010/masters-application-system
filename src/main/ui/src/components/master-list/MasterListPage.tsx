import * as React from 'react';
import Box from '@mui/material/Box';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TablePagination from '@mui/material/TablePagination';
import TableRow from '@mui/material/TableRow';
import TableSortLabel from '@mui/material/TableSortLabel';
import Toolbar from '@mui/material/Toolbar';
import Typography from '@mui/material/Typography';
import Paper from '@mui/material/Paper';
import {visuallyHidden} from '@mui/utils';
import {MasterPreviewDto} from "../../domain/masterPreviewDto";

import useDebounce from '../../hooks/usehooks-ts'
import { useNavigate } from 'react-router-dom';
import {Button, TextField} from "@mui/material";
import personService from "../../service/personService";
import CircularProgress from "@mui/material/CircularProgress";
import {ChangeEvent, useEffect} from "react";

interface Data {
    student: string;
    mentor: string;
    step: string;
    lastModified: string;
}

function descendingComparator<T>(a: T, b: T, orderBy: keyof T) {
    if (b[orderBy] < a[orderBy]) {
        return -1;
    }
    if (b[orderBy] > a[orderBy]) {
        return 1;
    }
    return 0;
}

type Order = 'asc' | 'desc';

function getComparator<Key extends keyof any>(
    order: Order,
    orderBy: Key,
): (
    a: { [key in Key]: number | string | Date },
    b: { [key in Key]: number | string | Date },
) => number {
    return order === 'desc'
        ? (a, b) => descendingComparator(a, b, orderBy)
        : (a, b) => -descendingComparator(a, b, orderBy);
}

// This method is created for cross-browser compatibility, if you don't
// need to support IE11, you can use Array.prototype.sort() directly
function stableSort<T>(array: readonly T[], comparator: (a: T, b: T) => number) {
    const stabilizedThis = array.map((el, index) => [el, index] as [T, number]);
    stabilizedThis.sort((a, b) => {
        const order = comparator(a[0], b[0]);
        if (order !== 0) {
            return order;
        }
        return a[1] - b[1];
    });
    return stabilizedThis.map((el) => el[0]);
}

interface HeadCell {
    disablePadding: boolean;
    id: keyof Data;
    label: string;
    numeric: boolean;
}

const headCells: readonly HeadCell[] = [
    {
        id: 'student',
        numeric: false,
        disablePadding: false,
        label: 'Студент',
    },
    {
        id: 'mentor',
        numeric: false,
        disablePadding: false,
        label: 'Ментор',
    },
    {
        id: 'step',
        numeric: false,
        disablePadding: false,
        label: 'Активен чекор',
    },
    {
        id: 'lastModified',
        numeric: false,
        disablePadding: false,
        label: 'Последно променето',
    },
];

interface EnhancedTableProps {
    onRequestSort: (event: React.MouseEvent<unknown>, property: keyof Data) => void;
    order: Order;
    orderBy: string;
    rowCount: number;
}

function EnhancedTableHead(props: EnhancedTableProps) {
    const {order, orderBy, rowCount, onRequestSort} =
        props;
    const createSortHandler =
        (property: keyof Data) => (event: React.MouseEvent<unknown>) => {
            onRequestSort(event, property);
        };

    return (
        <TableHead>
            <TableRow>
                {headCells.map((headCell) => (
                    <TableCell
                        key={headCell.id}
                        width={'25%'}
                        align={'center'}
                        padding={headCell.disablePadding ? 'none' : 'normal'}
                        sortDirection={orderBy === headCell.id ? order : false}
                    >
                        <TableSortLabel
                            active={orderBy === headCell.id}
                            direction={orderBy === headCell.id ? order : 'asc'}
                            onClick={createSortHandler(headCell.id)}
                        >
                            {headCell.label}
                            {orderBy === headCell.id ? (
                                <Box component="span" sx={visuallyHidden}>
                                    {order === 'desc' ? 'sorted descending' : 'sorted ascending'}
                                </Box>
                            ) : null}
                        </TableSortLabel>
                    </TableCell>
                ))}
            </TableRow>
        </TableHead>
    );
}

interface Prop {
    masters: MasterPreviewDto[];
    mastersNumber: number;
    loading: boolean;
    getMasters: Function,
    search: string,
    orderBy: string,
    order: string
}

export default function EnhancedTable(props: Prop) {
    const [order, setOrder] = React.useState<Order>((props.order === 'asc' || props.order === 'desc') ? props.order : 'desc' );
    console.log(props.order, props.orderBy)
    const [orderBy, setOrderBy] =
            React.useState<keyof Data>((props.orderBy == 'student' || props.orderBy == 'mentor' || props.orderBy == 'step' || props.orderBy == 'lastModified') ? props.orderBy : 'lastModified');
    const [page, setPage] = React.useState(0);
    const [rowsPerPage, setRowsPerPage] = React.useState(5);
    const navigate = useNavigate();
    const [searchValue, setSearchValue] = React.useState<string>(props.search)
    const debouncedValue = useDebounce<string>(searchValue, 500)

    const handleChange = (event: ChangeEvent<HTMLInputElement>) => {
        setSearchValue(event.target.value)
    }

    // Fetch API (optional)
    useEffect(() => {
        console.log(searchValue);
        // Do fetch here...
        // Triggers when "debouncedValue" changes
        props.getMasters(page, rowsPerPage, searchValue, orderBy, order);
    }, [debouncedValue])

    const handleRequestSort = (
        event: React.MouseEvent<unknown>,
        property: keyof Data,
    ) => {
        const isAsc = orderBy === property && order === 'asc';
        setOrder(isAsc ? 'desc' : 'asc');
        setOrderBy(property);
        props.getMasters(page, rowsPerPage, searchValue, orderBy, order);
    };

    const handleClick = (event: React.MouseEvent<unknown>, path: string) => {
        // const selectedIndex = selected.indexOf(name);
        navigate(path);
    };

    const handleChangePage = (event: unknown, newPage: number) => {
        setPage(newPage);
        console.log("change page :" + newPage + ", size " + rowsPerPage);
        props.getMasters(newPage, rowsPerPage, searchValue, orderBy, order);
    };

    const handleChangeRowsPerPage = (event: React.ChangeEvent<HTMLInputElement>) => {
        setRowsPerPage(parseInt(event.target.value, 10));
        setPage(0);
        console.log("change page :" + page + ", size " + parseInt(event.target.value, 10));
        props.getMasters(page, parseInt(event.target.value, 10), searchValue, orderBy, order);
    };

    const canCreateMaster = () => {
        let user = personService.getLoggedInUser();
        if (user == "")
            return false;
        return user["roles"][0] == "PROFESSOR";
    }

    // const isSelected = (name: string) => selected.indexOf(name) !== -1;

    // Avoid a layout jump when reaching the last page with empty rows.
    const emptyRows =
        page > 0 ? Math.max(0, (1 + page) * rowsPerPage - props.mastersNumber) : 0;

    return (
        <Box sx={{width: '100%'}}>
            {props.loading ?
                <div style={{minHeight: '85vh', minWidth: '50vw', marginTop: '200px', textAlign: 'center'}}>
                    <CircularProgress disableShrink size={70} />
                </div>
                :
            <Paper sx={{width: '100%', mb: 2}}>
                <Toolbar
                    sx={{
                        marginTop: '20px',
                        pl: {sm: 2},
                        pr: {xs: 1, sm: 1},
                    }}
                >
                    <Typography
                        sx={{flex: '1'}}
                        variant="h6"
                        id="tableTitle"
                        component="div"
                    >
                        Магистерски трудови
                    </Typography>
                    <TextField id="searchMasters" fullWidth defaultValue={props.search == "" ? "" : props.search}  label="Пребарај" variant="standard" sx={{ width: '50%', marginRight: '150px' }}
                               onChange={handleChange} />
                    {canCreateMaster() &&
                    <Button variant="contained" color={'primary'} onClick={(event) =>
                        handleClick(event, `/masterTopic`)}>Додади</Button>}
                </Toolbar>
                <TableContainer>
                    <Table
                        sx={{minWidth: 750}}
                        aria-labelledby="tableTitle"
                        size={'medium'}
                    >
                        <EnhancedTableHead
                            order={order}
                            orderBy={orderBy}
                            onRequestSort={handleRequestSort}
                            rowCount={props.masters.length}
                        />
                        <TableBody>
                            {/* if you don't need to support IE11, you can replace the `stableSort` call with:
              rows.slice().sort(getComparator(order, orderBy)) */}
                            {/*{stableSort<MasterPreviewDto>(props.masters, getComparator(order, orderBy))*/}
                            {/*    // .slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage)*/}
                            {props.masters.map((row, index) => {
                                    const labelId = `enhanced-table-checkbox-${index}`;

                                    return (
                                        <TableRow
                                            hover
                                            onClick={(event) => handleClick(event, `/master/${row.id}/details`)}
                                            key={row.id}
                                        >
                                            <TableCell
                                                component="th"
                                                id={labelId}
                                                align={'center'}
                                                width={'25%'}
                                                scope="row"
                                                padding="none"
                                            >
                                                {row.student}
                                            </TableCell>
                                            <TableCell
                                                align={'center'}>{row.mentor}</TableCell>
                                            <TableCell
                                                align={'center'}>{row.step}</TableCell>
                                            <TableCell
                                                align={'center'}>{row.lastModified}</TableCell>
                                        </TableRow>
                                    );
                                })}
                            {emptyRows > 0 && (
                                <TableRow
                                    style={{
                                        height: 53 * emptyRows,
                                    }}
                                >
                                    <TableCell colSpan={6}/>
                                </TableRow>
                            )}
                        </TableBody>
                    </Table>
                </TableContainer>
                <TablePagination
                    rowsPerPageOptions={[5, 10, 25]}
                    component="div"
                    count={props.mastersNumber}
                    rowsPerPage={rowsPerPage}
                    page={page}
                    onPageChange={handleChangePage}
                    onRowsPerPageChange={handleChangeRowsPerPage}
                />
            </Paper>
            }
        </Box>
    );
}
