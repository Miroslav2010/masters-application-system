import React from "react";
import {PageLayout} from "../../PageLayout";
import {MasterPreviewDto} from "../../domain/masterPreviewDto";
import masterService from "../../service/masterService";
import EnhancedTable from "./MasterListPage";

interface Props {
    // params: string;
}

interface State {
    masters: MasterPreviewDto[];
    totalNumberMasters: number;
    loading: boolean;
    searchValue: string;
    orderBy: string;
    order: string;
}

class MasterWrap extends React.Component<Props, State> {

    constructor(props: Props) {
        super(props);
        console.log(props);
        this.state = {
            masters: [],
            loading: true,
            totalNumberMasters: 0,
            searchValue: '',
            orderBy: 'lastModified',
            order: 'desc'
        }
    }


    componentDidMount() {
        console.log("did mount");
        this.getMasters(0, 5, "", "lastModified", "desc");
    }

    getMasters = (page: number, size: number, filter: string, orderBy: string, order: string) => {
        this.setState({
        loading: true
        });
        masterService.getAllMasters(page, size, filter, orderBy, order)
            .then(masters => {
                console.log(masters);
                this.setState({
                    masters: masters.masterPreviews,
                    totalNumberMasters: masters.mastersNumber,
                    loading: false,
                    searchValue: filter,
                    orderBy: orderBy,
                    order: order
                })
            });
    }

    render() {
        return (
            <PageLayout>
                <EnhancedTable masters={this.state.masters} loading={this.state.loading} getMasters={this.getMasters}
                               mastersNumber={this.state.totalNumberMasters} search={ this.state.searchValue }
                               orderBy={this.state.orderBy} order={this.state.order} />
            </PageLayout>
        )
    }

}

export default MasterWrap;