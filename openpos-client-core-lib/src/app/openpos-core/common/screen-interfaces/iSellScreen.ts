import { IMenuItem } from "../imenuitem";

export interface ISellScreen{
    name : string;
    type : string;
    backButton : IMenuItem;
    logoutButton : IMenuItem;
    template : string;
    sequenceNumber : number;
    locale : string;
    prompt : string;
    workstation : string;
    operatorName : string;
    icon : string;
    showScan : boolean;
    localMenuItems : IMenuItem[];
    theme : string;
}