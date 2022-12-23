import * as d3 from 'd3';
import { Hook, INode, IMarkmapOptions, IMarkmapJSONOptions } from 'markmap-common';
import { IMarkmapState, IMarkmapFlexTreeItem } from './types';
export declare const globalCSS: string;
interface IPadding {
    left: number;
    right: number;
    top: number;
    bottom: number;
}
declare type ID3SVGElement = d3.Selection<SVGElement, IMarkmapFlexTreeItem, HTMLElement, IMarkmapFlexTreeItem>;
declare function createViewHooks(): {
    transformHtml: Hook<[mm: Markmap, nodes: HTMLElement[]]>;
};
/**
 * A global hook to refresh all markmaps when called.
 */
export declare const refreshHook: Hook<[]>;
export declare const defaultColorFn: d3.ScaleOrdinal<string, string, never>;
export declare class Markmap {
    static defaultOptions: IMarkmapOptions;
    options: IMarkmapOptions;
    state: IMarkmapState;
    svg: ID3SVGElement;
    styleNode: d3.Selection<HTMLStyleElement, IMarkmapFlexTreeItem, HTMLElement, IMarkmapFlexTreeItem>;
    g: d3.Selection<SVGGElement, IMarkmapFlexTreeItem, HTMLElement, IMarkmapFlexTreeItem>;
    zoom: d3.ZoomBehavior<Element, unknown>;
    viewHooks: ReturnType<typeof createViewHooks>;
    revokers: (() => void)[];
    constructor(svg: string | SVGElement | ID3SVGElement, opts?: Partial<IMarkmapOptions>);
    getStyleContent(): string;
    updateStyle(): void;
    handleZoom(e: any): void;
    handlePan(e: WheelEvent): void;
    handleClick(_: MouseEvent, d: IMarkmapFlexTreeItem): void;
    initializeData(node: INode): void;
    setOptions(opts: Partial<IMarkmapOptions>): void;
    setData(data?: INode, opts?: Partial<IMarkmapOptions>): void;
    renderData(originData?: INode): void;
    transition<T extends d3.BaseType, U, P extends d3.BaseType, Q>(sel: d3.Selection<T, U, P, Q>): d3.Transition<T, U, P, Q>;
    /**
     * Fit the content to the viewport.
     */
    fit(): Promise<void>;
    /**
     * Pan the content to make the provided node visible in the viewport.
     */
    ensureView(node: INode, padding: Partial<IPadding> | undefined): Promise<void>;
    /**
     * Scale content with it pinned at the center of the viewport.
     */
    rescale(scale: number): Promise<void>;
    destroy(): void;
    static create(svg: string | SVGElement | ID3SVGElement, opts?: Partial<IMarkmapOptions>, data?: INode): Markmap;
}
export declare function deriveOptions(jsonOptions?: IMarkmapJSONOptions): Partial<IMarkmapOptions>;
export {};
