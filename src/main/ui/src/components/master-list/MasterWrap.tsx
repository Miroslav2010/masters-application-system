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
}

class MasterWrap extends React.Component<Props, State> {

    constructor(props: Props) {
        super(props);
        console.log(props);
        this.state = {
            masters: []
        }
    }


    componentDidMount() {
        console.log("did mount");
        masterService.getAllMasters()
            .then(masters => {
                console.log(masters);
                this.setState({
                    masters: masters
                })
            });
    }

    render() {
        return (
            <PageLayout>
                <EnhancedTable masters={this.state.masters} />
            </PageLayout>
        )
    }

}

export default MasterWrap;