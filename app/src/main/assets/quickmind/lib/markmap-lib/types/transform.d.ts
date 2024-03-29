import { Remarkable } from 'remarkable';
import { INode } from 'markmap-common';
import { ITransformResult, ITransformPlugin, IAssets, ITransformHooks, IFeatures } from './types';
import { plugins as builtInPlugins } from './plugins';
export { builtInPlugins };
export declare class Transformer {
    plugins: ITransformPlugin[];
    hooks: ITransformHooks;
    md: Remarkable;
    assetsMap: Record<string, IAssets>;
    constructor(plugins?: ITransformPlugin[]);
    buildTree(tokens: Remarkable.Token[]): INode;
    transform(content: string): ITransformResult;
    /**
     * Get all assets from enabled plugins or filter them by plugin names as keys.
     */
    getAssets(keys?: string[]): IAssets;
    /**
     * Get used assets by features object returned by `transform`.
     */
    getUsedAssets(features: IFeatures): IAssets;
}
