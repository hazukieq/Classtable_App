import { VType, VTYPE_ELEMENT, VTYPE_FUNCTION } from './consts';
export declare type VProps = {
    ref?: (el: Node) => void;
    innerHTML?: string;
    innerText?: string;
    textContent?: string;
    dangerouslySetInnerHTML?: {
        __html: string;
    };
    children?: VChildren;
} & {
    [key: string]: string | boolean | ((...args: unknown[]) => unknown);
};
export declare type VFunction = (props: VProps) => VNode;
export interface VNode {
    vtype: VType;
    type: string | VFunction;
    props: VProps;
}
export interface VElementNode extends VNode {
    vtype: typeof VTYPE_ELEMENT;
    type: string;
}
export interface VFunctionNode extends VNode {
    vtype: typeof VTYPE_FUNCTION;
    type: VFunction;
}
export declare type VChild = string | number | boolean | null | Node | VNode;
export declare type VChildren = VChild | VChildren[];
export interface MountEnv {
    isSvg: boolean;
}
export declare type DomNode = Node;
export declare type DomResult = DomNode | DomResult[];
