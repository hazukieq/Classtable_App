# Easy Usage of Markmap

## 1. Import markmap dependencies

```html
<!--import markmap dependencies-->
<script src="https://cdn.jsdelivr.net/npm/d3@7.8.2/dist/d3.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/markmap-lib@0.14.3/dist/browser/index.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/markmap-view@0.14.3/dist/index.min.js"></script>
```

## 2. Create a snippet which will be rendered to show later

### Here you can specify  svg's width  and height, or dynamically create a svg and intert into html page

```html
<!--create a SVG element with specified width and height-->
<svg id="markmap" style="width: 800px; height: 800px"></svg>
```

## 3. Get Transformer to parse raw data

```javascript
// transform raw data to markdown data
const Transformer=new window.markmap.Transformer;
const markdownData=Transformer.transform(rawData)
```

## 4. Get markdown assets like scripts and styles and load them

```javascript
// get all possible features that could be used later
var enabled={}
const keys=Object.keys(markdownData.features).filter((key)=>!enabled[key])
keys.forEach((key)=>enabled[key]=true)

// we got all assets like js、css now
const {styles,scripts}=Transformer.getAssets(keys)

//load assets
if(styles) markmap.loadCSS(styles)
if(scripts) markmap.loadJS(scripts)
```

## 5. Derive markmap and configs from markdown data

```javascript
//derive markmap data and frontmatter which embedded in markdown from markdown data
// root is  markmapContents
// frontmatter is markmapOptions
const markmapData=markdownData.root
const frontmatter=markdownData.frontmatter

// make sure frontmatter is empty or not,and then get raw data from that
/* es6 or later
const markmapOptions=frontmatter?.markmap
*/
// es5
const rawOptions=frontmatter === null || frontmatter === void 0 ? void 0 : frontmatter.markmap;
// formalize rawOptions which written in markdown to json options
const deriveOptions=window.markmap.deriveOptions
const markmapOptions=deriveOptions(rawOptions)
```

## 6. Load markmap with configs into svg snippet

```javascript
// create markmap svg with markmapData and markmapOptions
const Markmap=window.markmap.Markmap
const markmapSvg=Markmap.create("#markmap",markmapOptions,markmapData) 
```

### Actual Result

## Complete Codes here

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>网页</title>
    <!--import markmap dependencies-->
    <script src="https://cdn.jsdelivr.net/npm/d3@7.8.2/dist/d3.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/markmap-lib@0.14.3/dist/browser/index.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/markmap-view@0.14.3/dist/index.min.js"></script>
</head>
    
<body>
    <!--create a SVG element with specified width and height-->
    <svg id="markmap" style="width: 800px; height: 800px"></svg>
    
    <script>
        //test strings
        const rawData=
`
---
markmap:
  colorFreezeLevel: 2
---

# hello,markmap

## Links

- <https://markmap.js.org/>
- [GitHub](https://github.com/gera2ld/markmap)

## Related Projects

- [coc-markmap](https://github.com/gera2ld/coc-markmap)
- [gatsby-remark-markmap](https://github.com/gera2ld/gatsby-remark-markmap)

## Features

- links
- **strong** ~~del~~ *italic* ==highlight==
- multiline
  text
- \`inline code\`
-
    \`\`\`js
    console.log('code block');
    \`\`\`
- Katex
  - $x = {-b \pm \sqrt{b^2-4ac} \over 2a}$
  - [More Katex Examples](#?d=gist:af76a4c245b302206b16aec503dbe07b:katex.md)
- Now we can wrap very very very very long text based on \`maxWidth\` option
`
        
        // transform raw data to markdown data
        const Transformer=new window.markmap.Transformer;
        const markdownData=Transformer.transform(rawData)

        // get all possible features that could be used later
        var enabled={}
        const keys=Object.keys(markdownData.features).filter((key)=>!enabled[key])
        keys.forEach((key)=>enabled[key]=true)

        // we got all assets like js、css now
        const {styles,scripts}=Transformer.getAssets(keys)

        //load assets
        if(styles) markmap.loadCSS(styles)
        if(scripts) markmap.loadJS(scripts)

        //derive markmap data and frontmatter which embedded in markdown from markdown data
        // root is  markmapContents
        // frontmatter is markmapOptions
        const markmapData=markdownData.root
        const frontmatter=markdownData.frontmatter

        // make sure frontmatter is empty or not,and then get raw data from that
        /* es6 or later's usage
        const markmapOptions=frontmatter?.markmap
        */
        // es5 usage
        const rawOptions=frontmatter === null || frontmatter === void 0 ? void 0 : frontmatter.markmap;
        // formalize rawOptions which written in markdown to json options
        const deriveOptions=window.markmap.deriveOptions
        const markmapOptions=deriveOptions(rawOptions)

        // create markmap svg with markmapData and markmapOptions
        const Markmap=window.markmap.Markmap
        const markmapSvg=Markmap.create("#markmap",markmapOptions,markmapData) 
    </script>
</body>
</html>
```
