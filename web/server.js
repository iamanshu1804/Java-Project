import http from "node:http";
import { readFile } from "node:fs/promises";
import path from "node:path";
import { fileURLToPath } from "node:url";

const root = path.dirname(fileURLToPath(import.meta.url));
const types = {
  ".html": "text/html; charset=utf-8",
  ".css": "text/css; charset=utf-8",
  ".js": "text/javascript; charset=utf-8"
};

http.createServer(async (request, response) => {
  const requested = request.url === "/" ? "/index.html" : request.url.split("?")[0];
  const file = path.join(root, path.normalize(requested).replace(/^[/\\]+/, ""));

  if (!file.startsWith(root)) {
    response.writeHead(403).end("Forbidden");
    return;
  }

  try {
    const content = await readFile(file);
    response.writeHead(200, { "Content-Type": types[path.extname(file)] || "application/octet-stream" });
    response.end(content);
  } catch {
    response.writeHead(404).end("Not found");
  }
}).listen(process.env.PORT || 4173, () => {
  console.log(`University portal: http://localhost:${process.env.PORT || 4173}`);
});
