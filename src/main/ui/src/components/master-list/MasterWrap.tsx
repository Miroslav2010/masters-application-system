import React from "react";
import {PageLayout} from "../../PageLayout";
import {MasterPreviewDto} from "../../domain/masterPreviewDto";
import masterService from "../../service/masterService";
import EnhancedTable from "./MasterListPage";
import {MasterPreviewListDto} from "../../domain/MasterPreviewListDto";

interface Props {
    // params: string;
}

interface State {
    masters: MasterPreviewDto[];
    totalNumberMasters: number;
    loading: boolean;
    searchValue: string;
}

class MasterWrap extends React.Component<Props, State> {

    constructor(props: Props) {
        super(props);
        console.log(props);
        this.state = {
            masters: [],
            loading: true,
            totalNumberMasters: 0,
            searchValue: ''
        }
    }


    componentDidMount() {
        console.log("did mount");
        this.getMasters(0, 5, "");
    }

    getMasters = (page: number, size: number, filter: string) => {
        this.setState({
        loading: true
        });
        masterService.getAllMasters(page, size, filter)
            .then(masters => {
                console.log(masters);
                this.setState({
                    masters: masters.masterPreviews,
                    totalNumberMasters: masters.mastersNumber,
                    loading: false,
                    searchValue: filter
                })
            });
    }

    render() {
        return (
            <PageLayout>
                <EnhancedTable masters={this.state.masters} loading={this.state.loading} getMasters={this.getMasters}
                               mastersNumber={this.state.totalNumberMasters} search={ this.state.searchValue } />
            </PageLayout>
        )
    }

}

export default MasterWrap;