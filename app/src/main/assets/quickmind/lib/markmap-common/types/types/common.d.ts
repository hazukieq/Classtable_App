export interface IHierarchy<T> {
    type: string;
    /**
     * Additional data created on transformation.
     */
    payload?: {
        /**
         * The folding status of this node.
         *
         * 0 - not folded
         * 1 - folded
         * 2 - folded along with all its child nodes
         */
        fold?: number;
        /**
         * Index of list items.
         */
        index?: number;
        /**
         * Start index of an ordered list.
         */
        startIndex?: number;
        /**
         * First and last lines of the source generating the node.
         */
        lines?: [startLine: number, endLine: number];
    };
    /**
     * Store temporary data that helps rendering.
     */
    state?: {
        /**
         * An auto-increment unique ID for each node.
         */
        id?: number;
        /**
         * A dot separated sequence of the node and its ancestors.
         */
        path?: string;
        /**
         * The unique identifier of a node, supposed to be based on content.
         */
        key?: string;
        el?: HTMLElement;
        x0?: number;
        y0?: number;
        size?: [width: number, height: number];
    };
    children?: T[];
}
export interface INode extends IHierarchy<INode> {
    depth?: number;
    /**
     * HTML of the node content.
     */
    content: string;
}
export declare type JSScriptItem = {
    type: 'script';
    loaded?: Promise<void> | boolean;
    data: {
        src?: string;
        textContent?: string;
        async?: boolean;
        defer?: boolean;
    };
};
export declare type JSIIFEItem = {
    type: 'iife';
    loaded?: Promise<void> | boolean;
    data: {
        fn: (...args: unknown[]) => void;
        getParams?: (context: unknown) => void | unknown[];
    };
};
export declare type JSItem = JSScriptItem | JSIIFEItem;
export declare type CSSStyleItem = {
    type: 'style';
    loaded?: boolean;
    data: string;
};
export declare type CSSStylesheetItem = {
    type: 'stylesheet';
    loaded?: boolean;
    data: {
        href: string;
    };
};
export declare type CSSItem = CSSStyleItem | CSSStylesheetItem;
export interface IWrapContext<T extends unknown[], U> {
    thisObj: unknown;
    args: T;
    result?: U;
}
export interface IDeferred<T> {
    promise: Promise<T>;
    resolve: (value: T) => void;
    reject: (error?: unknown) => void;
}
export interface IMarkmapJSONOptions {
    color?: string[];
    colorFreezeLevel?: number;
    duration?: number;
    maxWidth?: number;
    initialExpandLevel?: number;
    extraJs?: string[];
    extraCss?: string[];
    zoom?: boolean;
    pan?: boolean;
}
export interface IMarkmapOptions {
    id?: string;
    autoFit: boolean;
    color: (node: INode) => string;
    duration: number;
    embedGlobalCSS: boolean;
    fitRatio: number;
    maxWidth: number;
    nodeMinHeight: number;
    paddingX: number;
    scrollForPan: boolean;
    spacingHorizontal: number;
    spacingVertical: number;
    initialExpandLevel: number;
    zoom: boolean;
    pan: boolean;
    style?: (id: string) => string;
}
