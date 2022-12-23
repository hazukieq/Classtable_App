import { JSItem, INode, IMarkmapOptions, IMarkmapJSONOptions } from 'markmap-common';
import { IAssets } from './types';
export declare function fillTemplate(root: INode | undefined, assets: IAssets, extra?: {
    baseJs?: JSItem[];
    jsonOptions?: IMarkmapJSONOptions;
    getOptions?: (jsonOptions: IMarkmapJSONOptions) => Partial<IMarkmapOptions>;
}): string;
