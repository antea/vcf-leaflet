import { UserConfigFn } from 'vite';
import { overrideVaadinConfig } from './vite.generated';
import { readFileSync, existsSync } from 'fs';
import { resolve } from 'path';

const customConfig: UserConfigFn = (env) => ({
  plugins: [
    {
      // Serve CAD worker files as raw static bytes, bypassing Vite's module
      // pipeline (HMR injection, asset URL transforms). Without this, Vite's
      // processing of import.meta.url inside the workers corrupts the
      // embedded WASM data: URL construction, producing a mangled HTTP URL
      // instead of a data: URL, which browsers block as a CORS error.
      name: 'cad-workers-raw-serve',
      configureServer(server) {
        server.middlewares.use((req, res, next) => {
          const url = req.url ?? '';
          if (!url.includes('/workers/') || !url.endsWith('.js')) {
            return next();
          }
          const filename = url.split('/').pop()!;
          const filePath = resolve(
            __dirname,
            'frontend/generated/jar-resources/workers',
            filename
          );
          if (!existsSync(filePath)) return next();
          const content = readFileSync(filePath);
          res.setHeader('Content-Type', 'application/javascript');
          res.setHeader('Cache-Control', 'no-cache');
          res.end(content);
        });
      }
    }
  ]
});

export default overrideVaadinConfig(customConfig);
